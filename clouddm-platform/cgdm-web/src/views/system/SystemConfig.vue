<template>
  <div>
    <PageHeader :title="$t('xi-tong-pei-zhi')"></PageHeader>
    <div class="container-wrapper">
      <Tabs value="name1" :animated="false">
        <TabPane :label="$t('ding-ding-gao-jing-qun-pei-zhi')" name="name1">
          <RadioGroup style="margin: 20px 0" v-model="dingdingConfig">
            <Radio label="default">{{ $t('shi-yong-mo-ren-pei-zhi') }}</Radio>
            <Radio label="custom">{{ $t('shi-yong-zi-ding-yi-pei-zhi') }}</Radio>
          </RadioGroup>
          <Form label-position="right" :label-width="100">
            <FormItem :label="$t('di-zhi')">
              <Input style="width: 400px"></Input>
            </FormItem>
          </Form>
        </TabPane>
        <TabPane :label="$t('duan-xin-fu-wu')" name="name2">
          <RadioGroup style="margin: 20px 0" v-model="dingdingConfig">
            <Radio label="default">{{ $t('shi-yong-mo-ren-pei-zhi') }}</Radio>
            <Radio label="custom">{{ $t('shi-yong-zi-ding-yi-pei-zhi') }}</Radio>
          </RadioGroup>
          <p style="margin: 10px 0">
            <a href="https://help.aliyun.com/product/44282.html?spm=5176.12212571.0.0.949b1cbeoJftHG" target="_blank">
              {{ $t('ru-he-pei-zhiali-yun-duan-xin-fu-wu') }}
            </a>
          </p>
          <Form label-position="right" :label-width="120">
            <FormItem :label="$t('yu-ming')">
              <Input style="width: 400px"></Input>
            </FormItem>
            <FormItem :label="$t('di-yu-0')">
              <Select style="width: 300px">
                <Option value="cn-hangzhou">cn-hangzhou</Option>
              </Select>
            </FormItem>
            <FormItem label="accessKeyId">
              <Input style="width: 400px"></Input>
            </FormItem>
            <FormItem label="accessSecret">
              <Input style="width: 400px"></Input>
            </FormItem>
            <FormItem :label="$t('zhu-ce-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('zhu-ce-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
            <FormItem :label="$t('deng-lu-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('deng-lu-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
            <FormItem :label="$t('ren-wu-yan-chi-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('ren-wu-yan-chi-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
            <FormItem :label="$t('shan-chu-ren-wu-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('shan-chu-ren-wu-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
            <FormItem :label="$t('qing-chu-wei-dian-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('qing-chu-wei-dian-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
            <FormItem :label="$t('ren-wu-yi-chang-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('ren-wu-yi-chang-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
            <FormItem :label="$t('xi-tong-yi-chang-mo-ban-code')">
              <Input style="width: 400px"></Input>
              <a style="margin-left: 10px">{{ $t('xi-tong-yi-chang-mo-ban-nei-rong-can-kao') }}</a>
            </FormItem>
          </Form>
        </TabPane>
        <TabPane :label="$t('fa-song-zhe-you-xiang-pei-zhi')" name="name3">
          <RadioGroup style="margin: 20px 0" v-model="dingdingConfig">
            <Radio label="default">{{ $t('shi-yong-mo-ren-pei-zhi') }}</Radio>
            <Radio label="custom">{{ $t('shi-yong-zi-ding-yi-pei-zhi') }}</Radio>
          </RadioGroup>
          <Form label-position="right" :label-width="100">
            <FormItem :label="$t('you-xiang-di-zhi')">
              <Input style="width: 400px"></Input>
              <span>:</span>
              <Input style="width: 100px"></Input>
            </FormItem>
            <FormItem :label="$t('yong-hu-ming')">
              <Input style="width: 300px"></Input>
            </FormItem>
            <FormItem :label="$t('mi-ma')">
              <Input type="password" password style="width: 300px"></Input>
            </FormItem>
            <FormItem :label="$t('fa-song-zhe')">
              <Input style="width: 300px"></Input>
            </FormItem>
          </Form>
        </TabPane>
      </Tabs>
      <div style="margin: 20px 0">
        <Button type="primary">{{ $t('bao-cun') }}</Button>
      </div>
    </div>
  </div>
</template>
<script>
import PageHeader from '@/components/function/PageHeader';

export default {
  components: {
    PageHeader
  },
  data() {
    return {
      name1: 'name1',
      dingdingConfig: 'default'
    };
  }
};
</script>
