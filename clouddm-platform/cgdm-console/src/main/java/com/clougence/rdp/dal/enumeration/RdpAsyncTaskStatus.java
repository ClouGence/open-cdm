package com.clougence.rdp.dal.enumeration;

/**
 * The type async_task do.
 * <pre>
 *   INIT ───> BLOCK ─────────────────────────────────────────────> CANCEL
 *     │         ↑                ↑               ↑                   ↑
 *     │         │             (cancel)         (skip)                │
 *     │         ↓                ↑               ↑                   │
 *     ╰───> WAIT_START ─────> RUNNING ─────>  FAILURE or COMPLETE    │
 *               ↑                ↓                                   │
 *               │             (pause)                                │
 *               │                ↓                                   │
 *               ╰─ (resume) ── PAUSE  ──── (skip) ───────────────────╯
 * </pre>
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
public enum RdpAsyncTaskStatus {
    INIT,
    BLOCK,
    WAIT_START,
    RUNNING,
    FAILURE,
    CANCEL,
    CANCELING,
    COMPLETE,
    PAUSE,
    PAUSING,
}
