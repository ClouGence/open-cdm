<template>
  <div class="add-datasource-steps">
    <a-breadcrumb style="margin-bottom: 20px">
      <a-breadcrumb-item>
        <a href="/#/system/ccdatasource">{{ $t('shu-ju-yuan-guan-li') }}</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>{{ $t('xin-zeng-shu-ju-yuan') }}</a-breadcrumb-item>
    </a-breadcrumb>
    <div v-if="step !== 1" class="info">
      <section>
        <div>{{ $t('bu-shu-lei-xing') }}</div>
        <div>{{ stepData[0].clusterEnvName }}</div>
      </section>
      <section>
        <div>{{ $t('shu-ju-ku-lei-xing') }}</div>
        <div>{{ stepData[0].dataSourceType }}</div>
      </section>
      <section>
        <div>{{ $t('fang-wen-ji-qi') }}</div>
        <div>{{ stepData[0].bindClusterName || stepData[0].bindClusterId }}</div>
      </section>
      <section>
        <div>{{ $t('yun-hang-huan-jing') }}</div>
        <div>{{ stepData[0].envName }}</div>
      </section>
    </div>
    <steps v-if="stepData[0].deployEnvType === CLUSTER_ENV.ALIBABA_CLOUD_HOSTED && step > 1" :step="step" style="margin-top: 20px" />
    <!--    <steps :step="step"/>-->
    <step-one v-if="step === 1" :handle-submit-data="handleSubmitData" :stepOneDataProps="stepData[0]" />
    <div v-else-if="step === 2" style="flex: 1">
      <step-two
        v-if="stepData[0].deployEnvType === CLUSTER_ENV.ALIBABA_CLOUD_HOSTED"
        :handle-back="handleBack"
        :handle-submit-data="handleSubmitData"
        :stepData="stepData"
        style="height: 100%; display: flex; flex-direction: column"
      />
      <step-self v-else :handle-back="handleBack" :stepData="stepData" />
    </div>
    <step-three v-else-if="step === 3" :handle-back="handleBack" :handle-submit-data="handleSubmitData" :stepData="stepData" />
    <step-four
      v-else-if="step === 4"
      :handle-back="handleBack"
      :handle-submit-data="handleSubmitData"
      :stepData="stepData"
      :handle-add-all-ds="handleAddAllDs"
    />
    <step-five v-else :handle-back="handleBack" :handle-submit-data="handleSubmitData" :stepData="stepData" :handle-add-all-ds="handleAddAllDs" />
  </div>
</template>
<script>
import { CLUSTER_ENV, WHITE_LIST_ADD_TYPE } from '@/const';
import Steps from '@/views/system/components/Steps';
import One from './One';
import Two from './Two';
import Three from './Three';
import Four from './Four';
import Five from './Five';
import Self from './Self';

export default {
  name: 'AddDataSource',
  components: {
    Steps,
    StepOne: One,
    StepTwo: Two,
    StepThree: Three,
    StepFour: Four,
    StepFive: Five,
    StepSelf: Self
  },
  data() {
    return {
      CLUSTER_ENV,
      step: 1,
      stepData: [{}, {}, {}, {}],
      newDatasource: {}
    };
  },
  methods: {
    async addDs(ds) {
      const { deployEnvType, dataSourceType, bindClusterId, envId } = this.stepData[0];
      const { region, instanceId, instanceDesc, auth, whiteList, ticket } = ds;
      const privateHost = ds.privateHost ? `${ds.privateHost.connectionString}:${ds.privateHost.port}` : '';
      const publicHost = ds.publicHost ? `${ds.publicHost.connectionString}:${ds.publicHost.port}` : '';
      console.log(ds.defaultHost);
      const defaultHost = ds.defaultHost === this.$t('nei-wang') ? privateHost : publicHost;
      const addIpWhiteList = whiteList.addIpWhiteList;

      const { platform, templates } = ticket;

      const templateArr = [];
      templates.forEach((t) => {
        templateArr.push({
          approvalType: platform,
          templateIdentity: t,
          templateName: this.templatesObj[t].approTemplateName
        });
      });
      const dsApproTemplatesJson = JSON.stringify(templateArr);
      const { userName, password } = auth.data;
      const addDsFOJson = {
        deployEnvType,
        dataSourceType,
        bindClusterId,
        envId,
        region,
        instanceId,
        instanceDesc,
        privateHost,
        publicHost,
        defaultHost,
        securityType: auth.type,
        autoCreateAccount: auth.auto,
        addIpWhiteList,
        dsPropsJson: JSON.stringify({
          userName,
          password
        }),
        dsApproTemplatesJson
      };

      if (addIpWhiteList) {
        addDsFOJson.whiteListAddType = WHITE_LIST_ADD_TYPE[whiteList.type];
      }

      const data = new FormData();
      data.append('addDsFOJson', JSON.stringify(addDsFOJson));

      if (auth.type === 'USER_PASSWD_WITH_TLS' && auth.data.tls) {
        data.append('securityFile', auth.data.tls, auth.data.tls.name);
      }

      if (auth.type === 'KERBEROS' && auth.data.kerberos && auth.data.keybat) {
        data.append('securityFile', auth.data.kerberos, auth.data.kerberos.name);
        data.append('secretFile', auth.data.keybat, auth.data.keybat.name);
      }

      const res = await this.$services.dmDataSourceAddDs({
        data,
        headers: {
          'content-type': 'multipart/form-data'
        },
        msg: this.$t('tian-jia-shu-ju-ku-cheng-gong')
      });

      return res;
    },
    async handleAddAllDs() {
      const dsArr = [];
      this.stepData[3].forEach((ds) => {
        dsArr.push(this.addDs(ds));
      });

      const res = await Promise.all(dsArr);
      let canNext = true;
      res.forEach((r) => {
        if (!r.success) {
          canNext = false;
        }
      });

      if (canNext) {
        await this.$router.push({ name: 'System_DataSource' });
      }
    },
    handleSubmitData(step, data) {
      this.stepData[this.step - 1] = data;
      this.step++;
    },
    handleBack(index, data) {
      this.step--;
      this.stepData[index] = data;
    }
  }
};
</script>

<style lang="less" scoped>
.add-datasource-steps {
  height: 100%;
  display: flex;
  flex-direction: column;

  .info {
    width: 100%;
    display: flex;
    border-right: 1px solid #dadada;

    section {
      display: flex;
      width: 25%;
      height: 32px;
      line-height: 32px;
      font-size: 12px;

      div:first-child {
        width: 100px;
        background: rgba(244, 244, 244, 1);
        text-align: right;
        padding-right: 10px;
        border-top: 1px solid #dadada;
        border-bottom: 1px solid #dadada;

        &:first-child {
          border-left: 1px solid #dadada;
        }
      }

      div:last-child {
        flex: 1;
        //border: 1px solid rgba(244, 244, 244, 1);
        padding-left: 10px;
        border: 1px solid #dadada;
        border-right: none;
      }
    }
  }
}
</style>
