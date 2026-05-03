import { instance } from '../noErrModal';
// TODO 为什么单独用noErrModal
// export const queryJobById = (param) => instance.post('datajob/queryjob', param);

export const sendCode = (param) => instance.post('verify/sendcode', param);
