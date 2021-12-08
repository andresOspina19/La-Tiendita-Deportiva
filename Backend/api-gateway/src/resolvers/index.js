const authResolver = require('./auth_resolver');
const inventoryResolver = require('./inventory_resolver');
const payResolver = require('./pay_resolver');

const lodash = require('lodash');

const resolvers = lodash.merge(inventoryResolver, authResolver, payResolver);

module.exports = resolvers;