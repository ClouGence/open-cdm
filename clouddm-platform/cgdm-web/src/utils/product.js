export const supportsCloudCanalBuild = false;
export const supportsCloudDMBuild = true;

export const filterFeaturesByBuild = (features = {}) => ({
  ...features,
  PRODUCT_CLOUD_CANAL: false,
  PRODUCT_CLOUD_DM: true
});

export const filterGlobalSettingByBuild = (globalSetting = {}) => ({
  ...globalSetting,
  features: filterFeaturesByBuild(globalSetting.features)
});
