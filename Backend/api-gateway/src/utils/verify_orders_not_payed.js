const serverConfig = require('../server');
const fetch = require('node-fetch');

const verifyOrders = async () => {
    try {
        let requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        };

        let ordersNotPayed = await fetch(`${serverConfig.inventory_api_url}/order/getAllOrdersNotPayed/`, requestOptions);
        await fetch(`${serverConfig.pay_api_url}/getAllPayments/`);
        ordersNotPayed = await ordersNotPayed.json();

        if (ordersNotPayed.length !== 0) {

            for (let i = 0; i < ordersNotPayed.length; i++) {
                let order = ordersNotPayed[i];

                let confirmOrDeleteOrder = {
                    username: order.username,
                    orderId: order.orderId
                }
    
                let status = (await fetch(`${serverConfig.pay_api_url}/getPaymentByToken/${order.paymentToken}?username=${order.username}`, requestOptions));
                status = (await status.json()).status                
                
                if (status == "approvedTransaction") {
                    let requestOptionsPut = {
                        method: 'PUT', headers: { "Content-Type": "application/json" },
                        body: JSON.stringify( confirmOrDeleteOrder )
                    };
                    fetch(`${serverConfig.inventory_api_url}/order/confirmOrderIsPayed/`, requestOptionsPut);
                    
                } else if (status == "declinedTransaction") {
                    let requestOptionsDelete = {
                        method: 'DELETE', headers: { "Content-Type": "application/json" },
                        body: JSON.stringify( confirmOrDeleteOrder )
                    };
                    fetch(
                        `${serverConfig.inventory_api_url}/order/deleteOrderDeclined/`,
                        requestOptionsDelete);
                }
            }
        }
        return true;
    }
    catch (error) {
        console.log(error);
        return false;
    }
}

module.exports = verifyOrders;