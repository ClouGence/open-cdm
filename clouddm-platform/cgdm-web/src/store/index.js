// src/store/index.js
import { createStore } from 'vuex';
import state from './state';
import mutations from './mutations';
import actions from './actions';
import getters from './getters';

const store = createStore({
  state,
  mutations,
  actions,
  getters
});

export default store;
