export function filterLeafGroup(leafGroup = [], attributes = {}) {
  if (!Array.isArray(attributes?.leafTypes)) return leafGroup;
  return leafGroup.filter((leaf) => attributes.leafTypes.includes(leaf.type || leaf));
}
