
var up = {

    init: function(environmentName) {
        this._identity = { public_key: null, private_key: null, principal: "Anonymous" }
    },

    environmentInfo: function() {
        axios.get('../api/environmentInfo)
            .then(response => console.log(response))
            .catch(error => console.log(error))
    },

    services: function() {
        axios.get('../api/services)
            .then(response => console.log(response))
            .catch(error => console.log(error))
    }

}


