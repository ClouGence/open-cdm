import { EventEmitter } from 'eventemitter3';

export default class Scope {
  constructor() {
    this.emitter = new EventEmitter();
  }

  $on(name, handler) {
    const emitter = this.emitter;

    const unbind = function () {
      emitter.off(name, handler);
    };

    emitter.on(name, handler);

    return unbind;
  }

  $emit(name, data) {
    this.emitter.emit(name, data);
  }
}
