const authResolver = require('./auth_resolver');
const inventoryResolver = require('./inventory_resolver');
const payResolver = require('./pay_resolver');
const cartResolver = require('./cart_resolver');
const orderResolver = require('./order_resolver');

const lodash = require('lodash');
const cartTypeDefs = require('../typeDefs/cart_type_defs');

const resolvers = lodash.merge(inventoryResolver, authResolver, payResolver, cartResolver, orderResolver);

module.exports = resolvers;