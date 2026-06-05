import { mapState } from 'vuex';
import { sm2 } from 'sm-crypto';

export const encryptMixin = {
  computed: {
    ...mapState(['publicKey'])
  },
  methods: {
    passwordEncrypt(password) {
      return sm2.doEncrypt(password, this.publicKey);
    }
  }
};
