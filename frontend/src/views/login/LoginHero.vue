<template>
  <div class="login-hero-panel" :class="{ 'is-dark': theme === 'dark' }" aria-hidden="true">
    <div class="hero-fountain-stage">
      <svg class="stage-svg" viewBox="0 0 480 480" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <radialGradient id="sourceGlow">
            <stop offset="0%" stop-color="#3ecf8e" stop-opacity="0.22" />
            <stop offset="40%" stop-color="#3ecf8e" stop-opacity="0.08" />
            <stop offset="100%" stop-color="#3ecf8e" stop-opacity="0" />
          </radialGradient>
        </defs>
        <circle cx="240" cy="240" r="60" fill="url(#sourceGlow)" class="source-aura" />
      </svg>

      <div class="fountain-field" :style="{ transform: `rotate(${globalRotate}deg)` }">
        <div
          v-for="card in fountainCards"
          :key="card.id"
          class="fountain-card"
          :style="{
            left: `${card.px}px`,
            top: `${card.py}px`,
            opacity: card.opacity,
            transform: `scale(${card.scale}) rotate(${-globalRotate}deg)`
          }"
        >
          <CustomIcon :type="card.type" :size="card.iconSize" leftMargin="0" rightMargin="0" />
        </div>
      </div>
    </div>

    <div class="hero-capabilities" aria-hidden="true">
      <p class="capability-text">
        <template v-for="(item, idx) in capabilityLinks">
          <a :key="item.key" :href="item.url" target="_blank" rel="noopener" class="capability-link">{{ $t(item.key) }}</a>
          <span v-if="idx < capabilityLinks.length - 1" :key="'divider-' + item.key" class="cap-divider" aria-hidden="true"></span>
        </template>
      </p>
    </div>
  </div>
</template>

<script>
const FOUNTAIN_TYPES = [
  'MySQL',
  'PostgreSQL',
  'Oracle',
  'MongoDB',
  'Redis',
  'ClickHouse',
  'Kafka',
  'TiDB',
  'SQLServer',
  'ElasticSearch',
  'MariaDB',
  'Hive',
  'Doris',
  'OceanBase',
  'StarRocks',
  'Db2',
  'Dameng',
  'KingbaseES',
  'Greenplum',
  'TDengine',
  'DynamoDB',
  'Redshift',
  'AuroraMySQL',
  'AuroraPostgreSQL',
  'RDSforMySQL',
  'RDSforPostgreSQL',
  'PolarDbMySQL',
  'PolarDBPg',
  'PolarDbX',
  'GaussDB',
  'GaussDBForMySQL',
  'GaussDBForOpenGauss',
  'TdsqlMySQL',
  'TdsqlCMySQL',
  'AdbForMySQL',
  'ADBforPG',
  'ObForOracle',
  'AliKafka',
  'AutoMQ',
  'AmazonMSK',
  'AMQP',
  'ElastiCache'
];

const CARD_COUNT = 30;
const CENTER_X = 240;
const CENTER_Y = 240;
const MAX_DIST = 260;

let nextId = 0;

function pickType(exclude) {
  const pool = FOUNTAIN_TYPES.filter((t) => !exclude.has(t));
  if (!pool.length) {
    return FOUNTAIN_TYPES[Math.floor(Math.random() * FOUNTAIN_TYPES.length)];
  }
  return pool[Math.floor(Math.random() * pool.length)];
}

function spawnCard(excludeTypes) {
  const angle = Math.random() * Math.PI * 2;
  const speed = 35 + Math.random() * 55;
  const maxAge = 1.8 + Math.random() * 2.4;
  const type = excludeTypes ? pickType(excludeTypes) : FOUNTAIN_TYPES[Math.floor(Math.random() * FOUNTAIN_TYPES.length)];
  const iconSize = (28 + Math.random() * 16).toFixed(0) + 'px';
  return {
    id: nextId++,
    type,
    iconSize,
    angle,
    speed,
    age: Math.random() * maxAge,
    maxAge,
    px: CENTER_X,
    py: CENTER_Y,
    scale: 0.1,
    opacity: 0
  };
}

export default {
  name: 'LoginHero',
  computed: {
    theme() {
      return this.$store.state.theme || 'light';
    }
  },
  data() {
    const cards = [];
    const usedTypes = new Set();
    for (let i = 0; i < CARD_COUNT; i++) {
      const card = spawnCard(usedTypes);
      usedTypes.add(card.type);
      cards.push(card);
    }
    return {
      fountainCards: cards,
      globalRotate: 0,
      rafId: null,
      lastTime: 0,
      capabilityLinks: [
        { key: 'shu-ju-yuan-guan-li', url: 'https://www.cdmgr.com/docs/operation/dsmanager' },
        { key: 'an-quan-gui-ze', url: 'https://www.cdmgr.com/docs/datamask/datamask_about' },
        { key: 'gong-dan-shen-pi-liu-cheng', url: 'https://www.cdmgr.com/docs/approval/approval_about' },
        { key: 'sql-cha-xun', url: 'https://www.cdmgr.com/docs/console/console_editor' },
        { key: 'ci-cd-bian-geng-fa-bu', url: 'https://www.cdmgr.com/docs/devops/devops_about' }
      ]
    };
  },
  mounted() {
    this.lastTime = performance.now();
    this.tick(this.lastTime);
  },
  beforeUnmount() {
    if (this.rafId) {
      cancelAnimationFrame(this.rafId);
      this.rafId = null;
    }
  },
  methods: {
    tick(now) {
      const dt = Math.min((now - this.lastTime) / 1000, 0.1);
      this.lastTime = now;

      for (let i = 0; i < this.fountainCards.length; i++) {
        const card = this.fountainCards[i];
        card.age += dt;

        if (card.age >= card.maxAge) {
          const activeTypes = new Set(this.fountainCards.map((c) => c.type));
          activeTypes.delete(card.type);
          const fresh = spawnCard(activeTypes);
          fresh.id = card.id;
          fresh.age = Math.random() * 0.3;
          this.fountainCards[i] = fresh;
          continue;
        }

        const progress = card.age / card.maxAge;
        const dist = card.speed * card.age;
        const wobble = Math.sin(card.age * 3.5 + card.angle) * 18 * progress;
        card.px = CENTER_X + Math.cos(card.angle) * dist + Math.cos(card.angle + Math.PI / 2) * wobble;
        card.py = CENTER_Y + Math.sin(card.angle) * dist + Math.sin(card.angle + Math.PI / 2) * wobble;

        // Scale: tiny at center → continuously grow to edge
        card.scale = 0.12 + progress * 0.88;

        // Opacity
        if (progress < 0.06) {
          card.opacity = progress / 0.06;
        } else if (progress > 0.7) {
          card.opacity = 1 - (progress - 0.7) / 0.3;
        } else {
          card.opacity = 1;
        }
      }

      this.globalRotate = (this.globalRotate + dt * 8) % 360;

      this.rafId = requestAnimationFrame(this.tick);
    }
  }
};
</script>

<style lang="less" scoped>
.login-hero-panel {
  width: 100%;
  max-width: 520px;
  animation: hero-enter 0.7s cubic-bezier(0.22, 1, 0.36, 1) both;
}

.hero-fountain-stage {
  position: relative;
  width: 100%;
  max-width: 480px;
  aspect-ratio: 1;
  margin: 0 auto;
  overflow: hidden;
}

.stage-svg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.source-aura {
  animation: aura-pulse 3s ease-in-out infinite;
}

.fountain-field {
  position: absolute;
  inset: 0;
  will-change: transform;
}

.fountain-card {
  position: absolute;
  width: 46px;
  height: 46px;
  margin: -23px 0 0 -23px;
  border-radius: 12px;
  border: 1px solid #dfdfdf;
  background: #ffffff;
  box-shadow: 0 4px 16px rgba(23, 23, 23, 0.06);
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: none;
  will-change: left, top, opacity, transform;
}

@keyframes aura-pulse {
  0%,
  100% {
    opacity: 0.4;
  }
  50% {
    opacity: 0.85;
  }
}

@keyframes hero-enter {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.hero-capabilities {
  margin-top: 32px;
  text-align: center;

  .capability-text {
    color: #707070;
    font-size: 14px;
    font-weight: 400;
    line-height: 1.5;
    letter-spacing: 0.02em;
    margin: 0;

    .capability-link {
      color: inherit;
      text-decoration: none;
      white-space: nowrap;
      transition: color 0.2s ease;

      &:hover {
        color: var(--login-emerald-deep, #24b47e);
      }
    }

    .cap-divider::after {
      content: '\00B7';
      margin: 0 6px;
      color: #d4d4d4;
    }
  }
}

.login-hero-panel.is-dark {
  .fountain-card {
    background: #1c1c1c;
    border-color: #333;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.25);
  }

  .hero-capabilities .capability-text {
    color: #707070;
  }
}

@media (max-width: 960px) {
  .login-hero-panel {
    display: none;
  }
}
</style>
