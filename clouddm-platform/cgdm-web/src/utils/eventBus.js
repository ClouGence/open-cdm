import mitt from 'mitt';

const emitter = mitt();

export default {
  on: emitter.on,
  off: emitter.off,
  emit: emitter.emit
};
