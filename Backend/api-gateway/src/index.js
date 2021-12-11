const { ApolloServer } = require('apollo-server');
const typeDefs = require('./typeDefs');
const resolvers = require('./resolvers');
const InventoryAPI = require('./dataSources/inventory_api');
const AuthAPI = require('./dataSources/auth_api');
const PayAPI = require('./dataSources/pay_api');
const authentication = require('./utils/authentication');

const server = new ApolloServer({
    context: async ({ req }) => ({
        userIdToken: await authentication( req ),
    }),
    typeDefs,
    resolvers,
    dataSources: () => ({
        inventoryAPI: new InventoryAPI(),
        authAPI: new AuthAPI(),
        payAPI: new PayAPI(),
    }),
    introspection: true,
    playground: true
});

server.listen(process.env.PORT || 4000).then(({ url }) => {
    console.log(`🚀  Server ready at ${url}`);
});