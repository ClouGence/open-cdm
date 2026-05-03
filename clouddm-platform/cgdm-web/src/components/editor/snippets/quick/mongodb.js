/* eslint-disable no-template-curly-in-string */
export const MONGODB_QUICK = [
  {
    label: 'getCollection',
    insertText: 'db.getCollection("${1:collection}")',
    detail: 'db.getCollection("collection")'
  },
  {
    label: 'getCollectionNames',
    insertText: 'db.getCollectionNames()',
    detail: 'db.getCollectionNames()'
  },
  {
    label: 'createCollection',
    insertText: 'db.createCollection("${1:collection}", {\n  ${2:capped: false}\n})',
    detail: 'db.createCollection("collection", { options })'
  },
  {
    label: 'show collections',
    insertText: 'show collections',
    detail: 'show collections'
  },
  {
    label: 'show dbs',
    insertText: 'show dbs',
    detail: 'show dbs'
  },
  {
    label: 'use database',
    insertText: 'use ${1:database}',
    detail: 'use database'
  },
  {
    label: 'find',
    insertText: 'db.${1:collection}.find({\n  ${2:field}: ${3:value}\n})',
    detail: 'db.collection.find({ field: value })'
  },
  {
    label: 'findOne',
    insertText: 'db.${1:collection}.findOne({\n  ${2:field}: ${3:value}\n})',
    detail: 'db.collection.findOne({ field: value })'
  },
  {
    label: 'insertOne',
    insertText: 'db.${1:collection}.insertOne({\n  ${2:field}: ${3:value}\n})',
    detail: 'db.collection.insertOne({ field: value })'
  },
  {
    label: 'insertMany',
    insertText: 'db.${1:collection}.insertMany([\n  {\n    ${2:field}: ${3:value}\n  }\n])',
    detail: 'db.collection.insertMany([{ field: value }])'
  },
  {
    label: 'updateOne',
    insertText: 'db.${1:collection}.updateOne(\n  { ${2:field}: ${3:value} },\n  { $$set: { ${4:field}: ${5:newValue} } }\n)',
    detail: 'db.collection.updateOne({ field: value }, { $set: { field: newValue } })'
  },
  {
    label: 'updateMany',
    insertText: 'db.${1:collection}.updateMany(\n  { ${2:field}: ${3:value} },\n  { $$set: { ${4:field}: ${5:newValue} } }\n)',
    detail: 'db.collection.updateMany({ field: value }, { $set: { field: newValue } })'
  },
  {
    label: 'deleteOne',
    insertText: 'db.${1:collection}.deleteOne({\n  ${2:field}: ${3:value}\n})',
    detail: 'db.collection.deleteOne({ field: value })'
  },
  {
    label: 'deleteMany',
    insertText: 'db.${1:collection}.deleteMany({\n  ${2:field}: ${3:value}\n})',
    detail: 'db.collection.deleteMany({ field: value })'
  },
  {
    label: 'aggregate',
    insertText:
      'db.${1:collection}.aggregate([\n  { $$match: { ${2:field}: ${3:value} } },\n  { $$group: { _id: "$$${4:field}", count: { $$sum: 1 } } }\n])',
    detail: 'db.collection.aggregate([{ $match: {...} }, { $group: {...} }])'
  },
  {
    label: 'count',
    insertText: 'db.${1:collection}.countDocuments({\n  ${2:field}: ${3:value}\n})',
    detail: 'db.collection.countDocuments({ field: value })'
  },
  {
    label: 'distinct',
    insertText: 'db.${1:collection}.distinct("${2:field}", {\n  ${3:field}: ${4:value}\n})',
    detail: 'db.collection.distinct("field", { field: value })'
  },
  {
    label: 'createIndex',
    insertText: 'db.${1:collection}.createIndex({ ${2:field}: ${3:1} })',
    detail: 'db.collection.createIndex({ field: 1 })'
  },
  {
    label: 'find with sort',
    insertText: 'db.${1:collection}.find({\n  ${2:field}: ${3:value}\n}).sort({ ${4:field}: ${5:1} })',
    detail: 'db.collection.find({ field: value }).sort({ field: 1 })'
  },
  {
    label: 'find with limit',
    insertText: 'db.${1:collection}.find({\n  ${2:field}: ${3:value}\n}).limit(${4:10})',
    detail: 'db.collection.find({ field: value }).limit(10)'
  },
  {
    label: 'find with projection',
    insertText: 'db.${1:collection}.find(\n  { ${2:field}: ${3:value} },\n  { ${4:field}: 1, _id: 0 }\n)',
    detail: 'db.collection.find({ field: value }, { field: 1, _id: 0 })'
  },
  {
    label: 'replaceOne',
    insertText: 'db.${1:collection}.replaceOne(\n  { ${2:field}: ${3:value} },\n  {\n    ${4:field}: ${5:newValue}\n  }\n)',
    detail: 'db.collection.replaceOne({ field: value }, { field: newValue })'
  },
  {
    label: 'createIndex',
    insertText: 'db.${1:collection}.createIndex(\n  { ${2:field}: ${3:1} },\n  { ${4:name: "${5:index_name}"} }\n)',
    detail: 'db.collection.createIndex({ field: 1 }, { name: "index_name" })'
  },
  {
    label: 'getIndexes',
    insertText: 'db.${1:collection}.getIndexes()',
    detail: 'db.collection.getIndexes()'
  },
  {
    label: 'drop collection',
    insertText: 'db.${1:collection}.drop()',
    detail: 'db.collection.drop()'
  },
  {
    label: 'stats',
    insertText: 'db.${1:collection}.stats()',
    detail: 'db.collection.stats()'
  },
  {
    label: 'bulkWrite',
    insertText:
      'db.${1:collection}.bulkWrite([\n  { insertOne: { document: { ${2:field}: ${3:value} } } },\n  { updateOne: { filter: { ${4:field}: ${5:value} }, update: { $$set: { ${6:field}: ${7:newValue} } } } }\n])',
    detail: 'db.collection.bulkWrite([operations])'
  },
  {
    label: 'aggregate with $match and $group',
    insertText:
      'db.${1:collection}.aggregate([\n  {\n    $$match: {\n      ${2:field}: ${3:value}\n    }\n  },\n  {\n    $$group: {\n      _id: "$$${4:field}",\n      ${5:total}: { $$sum: ${6:1} }\n    }\n  }\n])',
    detail: 'db.collection.aggregate([{ $match: {...} }, { $group: {...} }])'
  },
  {
    label: 'aggregate with $lookup',
    insertText:
      'db.${1:collection}.aggregate([\n  {\n    $$lookup: {\n      from: "${2:otherCollection}",\n      localField: "${3:localField}",\n      foreignField: "${4:foreignField}",\n      as: "${5:outputArray}"\n    }\n  }\n])',
    detail: 'db.collection.aggregate([{ $lookup: {...} }])'
  },
  {
    label: 'find with $regex',
    insertText: 'db.${1:collection}.find({\n  ${2:field}: { $$regex: /${3:pattern}/, $$options: "${4:i}" }\n})',
    detail: 'db.collection.find({ field: { $regex: /pattern/, $options: "i" } })'
  },
  {
    label: 'find with $in',
    insertText: 'db.${1:collection}.find({\n  ${2:field}: { $$in: [${3:value1}, ${4:value2}] }\n})',
    detail: 'db.collection.find({ field: { $in: [...] } })'
  },
  {
    label: 'find with $exists',
    insertText: 'db.${1:collection}.find({\n  ${2:field}: { $$exists: ${3:true} }\n})',
    detail: 'db.collection.find({ field: { $exists: true } })'
  },
  {
    label: 'updateOne with $inc',
    insertText: 'db.${1:collection}.updateOne(\n  { ${2:field}: ${3:value} },\n  { $$inc: { ${4:field}: ${5:1} } }\n)',
    detail: 'db.collection.updateOne({ field: value }, { $inc: { field: 1 } })'
  },
  {
    label: 'findOneAndUpdate',
    insertText:
      'db.${1:collection}.findOneAndUpdate(\n  { ${2:field}: ${3:value} },\n  { $$set: { ${4:field}: ${5:newValue} } },\n  { returnDocument: "after", upsert: ${6:false} }\n)',
    detail: 'db.collection.findOneAndUpdate({ field: value }, { $set: {...} }, { options })'
  }
];
