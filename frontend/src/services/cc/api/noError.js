import { instance } from '../noErrModal';
// TODO, Why NoErrModal?
// export const queryJobById = (param) => instance.post('datajob/queryjob', param);

export const sendCode = (param) => instance.post('verify/sendcode', param);
