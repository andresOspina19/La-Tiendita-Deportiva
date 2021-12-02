const authResolver = require('./auth_resolver');
const inventoryResolver = require('./inventory_resolver');

const lodash = require('lodash');

const resolvers = lodash.merge(inventoryResolver, authResolver);

module.exports = resolvers;