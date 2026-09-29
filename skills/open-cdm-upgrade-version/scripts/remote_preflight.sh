#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF'
Usage: remote_preflight.sh --user <ssh-user> --host <ssh-host>

Run a read-only Open-CDM inventory over SSH. Both arguments are required.
EOF
}

ssh_user=""
ssh_host=""

while (($#)); do
  case "$1" in
    --user)
      [[ $# -ge 2 ]] || { echo "missing value for --user" >&2; exit 2; }
      ssh_user=$2
      shift 2
      ;;
    --host)
      [[ $# -ge 2 ]] || { echo "missing value for --host" >&2; exit 2; }
      ssh_host=$2
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "unknown argument: $1" >&2
      usage >&2
      exit 2
      ;;
  esac
done

if [[ -z "$ssh_user" || -z "$ssh_host" ]]; then
  echo "both --user and --host are required; do not infer them from history" >&2
  exit 2
fi

if [[ "$ssh_user" == -* || "$ssh_host" == -* || "$ssh_user" == *$'\n'* || "$ssh_host" == *$'\n'* ]]; then
  echo "invalid SSH username or host" >&2
  exit 2
fi

target="${ssh_user}@${ssh_host}"

ssh -o BatchMode=yes -o ConnectTimeout=10 "$target" 'bash -s' <<'REMOTE'
set -u

echo "== host =="
hostname
date -Is
uname -srmo
df -h / /home /var/lib/docker 2>/dev/null | awk '!seen[$1]++'

echo "== docker =="
if command -v docker >/dev/null 2>&1; then
  docker --version
  docker compose version 2>/dev/null || true
  docker compose ls 2>/dev/null || true
  docker ps -a --filter 'name=cgdm' --format 'table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}'

  if docker inspect cgdm-alone >/dev/null 2>&1; then
    docker inspect cgdm-alone --format 'Image={{.Config.Image}} Restart={{.HostConfig.RestartPolicy.Name}} Network={{.HostConfig.NetworkMode}}'
    docker inspect cgdm-alone --format 'Ports={{json .HostConfig.PortBindings}}'
    docker inspect cgdm-alone --format 'Mounts={{json .Mounts}}'
    echo "Environment keys:"
    docker inspect cgdm-alone --format '{{range .Config.Env}}{{println .}}{{end}}' | sed 's/=.*//' | sort
    printf 'Version='
    docker exec cgdm-alone sh -lc 'cat /root/cgdm/alone/conf/version 2>/dev/null || true'
  fi
else
  echo "docker: not installed"
fi

echo "== tgz candidates =="
pgrep -l -f 'Dm(Alone|Console|Sidecar)Launcher' 2>/dev/null || true
find /root /opt /srv /home -maxdepth 5 -type f -path '*/cgdm-*/bin/startup.sh' -print 2>/dev/null | head -50

echo "== http =="
printf 'localhost:8222='
curl -sS -o /dev/null -w '%{http_code}\n' --max-time 5 http://127.0.0.1:8222/ || echo unavailable
REMOTE
