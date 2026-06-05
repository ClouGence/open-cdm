<template>
  <div>
    <p>
      <span class="job-header-name-desc basic-info-label" style="margin-right: 10px">HOST</span>
      <span class="basic-info-value mt-2">
        {{ dataSourceDetail.host || dataSourceDetail.privateHost || dataSourceDetail.publicHost }}
      </span>
      <GoDmButton class="mt-2" style="margin-left: 8px"></GoDmButton>
    </p>
    <Row style="margin-top: 8px">
      <Col :span="12">
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('bu-shu-lei-xing-0') }}</span>
          <span class="basic-info-value">
            {{ DATASOURCE_DEPLOY_TYPE_I18N[dataSourceDetail.deployType] }}
          </span>
        </p>
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('shi-li-id-0') }}</span>
          <span class="basic-info-value">{{ dataSourceDetail.instanceId }}</span>
        </p>
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('lei-xing-0') }}</span>
          <span class="basic-info-value">
            <CustomIcon :type="dataSourceDetail.dataSourceType" :instanceType="dataSourceDetail.deployType"></CustomIcon>
          </span>
        </p>
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('ren-zheng-fang-shi-0') }}</span>
          <span class="basic-info-value">
            {{ MappingUtil.securityType[dataSourceDetail.securityType] }}
          </span>
        </p>
      </Col>
      <Col :span="12">
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('miao-shu-0') }}</span>
          <span class="basic-info-value">{{ dataSourceDetail.instanceDesc }}</span>
        </p>
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('ban-ben-hao-0') }}</span>
          <span class="basic-info-value">{{ dataSourceDetail.version }}</span>
        </p>
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('shu-ju-yuan-shu-zi-id-0') }}</span>
          <span class="basic-info-value">{{ dataSourceDetail.id }}</span>
        </p>
        <p class="mb-1">
          <span class="basic-info-label">{{ $t('tian-jia-shi-jian') }}</span>
          <span class="basic-info-value">
            {{ dataSourceDetail.gmtCreate ? fecha.format(new Date(dataSourceDetail.gmtCreate), 'YYYY-MM-DD HH:mm:ss') : '' }}
          </span>
        </p>
      </Col>
    </Row>
    <div class="datasource-info-block" v-if="dataSourceDetail.extraVO && dataSourceDetail.dataSourceType === 'Hive'">
      <p style="margin-top: 8px">
        <span class="basic-info-label">{{ $t('hdfs-host-0') }}</span>
        <span class="basic-info-value">
          {{ dataSourceDetail.extraVO.hdfsIp + ':' + dataSourceDetail.extraVO.hdfsPort }}
        </span>
      </p>
      <p>
        <span class="basic-info-label">{{ $t('shu-cang-lu-jing') }}</span>
        <span class="basic-info-value">{{ dataSourceDetail.extraVO.hdfsDwDir }}</span>
      </p>
      <p>
        <span class="basic-info-label">{{ $t('hdfs-principal-0') }}</span>
        <span class="basic-info-value">{{ dataSourceDetail.extraVO.hdfsPrincipal }}</span>
      </p>
      <p>
        <span class="basic-info-label">{{ $t('hadoop-user') }}</span>
        <span class="basic-info-value">{{ dataSourceDetail.extraVO.hadoopUser }}</span>
      </p>
    </div>
    <div class="datasource-info-block">
      <!--            <Divider class="datasource-info-block-title" orientation="left">账号信息</Divider>-->
      <p style="margin-top: 8px">
        <span class="basic-info-label">{{ $t('zhang-hao-0') }}</span>
        <span class="basic-info-value">
          {{ dataSourceDetail.accountName ? dataSourceDetail.accountName : $t('zan-wu') }}
        </span>
      </p>
    </div>
  </div>
</template>
<script>
import fecha from 'fecha';
import MappingUtil from '@/views/util';
import { DATASOURCE_DEPLOY_TYPE_I18N } from '@/const';
import GoDmButton from '@/components/GoDmButton';

export default {
  computed: {
    DATASOURCE_DEPLOY_TYPE_I18N() {
      return DATASOURCE_DEPLOY_TYPE_I18N;
    }
  },
  components: { GoDmButton },
  props: {
    dataSourceDetail: Object
  },
  data() {
    return {
      fecha,
      MappingUtil,
      desc: ''
    };
  }
};
</script>
