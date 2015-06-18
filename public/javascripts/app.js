var app = angular.module('app', ['ngRoute', 'ngWebsocket'])
    .constant("apiUrl", "http://localhost:9000\:9000/api/v1")
    .config(["$routeProvider", function ($routeProvider) {
        return $routeProvider.when("/", {
            templateUrl: "/views/responseCreate",
            controller: "ResponsesCollectionController"
        }).when("/responses", {
            templateUrl: "/views/responses",
            controller: "ResponsesController"
        }).when("/fields", {
            templateUrl: "/views/fieldsList",
            controller: "FieldsListController"
        }).when("/create", {
            templateUrl: "/views/fieldCreate",
            controller: "FieldCreateController"
        }).when("/field/:id", {
            templateUrl: "/views/fieldEdit",
            controller: "FieldEditController"
        }).when("/success",{
            templateUrl: "/views/success"
        }).otherwise({
            redirectTo: "/"
        });
    }
    ]).config([
        "$locationProvider", function ($locationProvider) {
            return $locationProvider.html5Mode(true).hashPrefix("!");
        }
    ]);

app.controller("FieldsListController", ["$scope", "$location", "$http", "$route", function ($scope, $location, $http, $route) {
    $scope.fields = [];
    var load = function(){
        $http.get("/api/v1/field").
            success(function (data, status, headers, config) {
                $scope.fields = data;
            });
    };
    load();
    $scope.type = types;
    $scope.addField = function () {
        $location.path('/create');
    };
    $scope.delField = function (field) {
        $http.delete('/api/v1/field/' + field.id).success($scope.$apply(load()));
    }
}]);

app.controller("FieldCreateController", ["$scope", "$location", "$http", function ($scope, $location, $http) {
    $scope.item = {'required' : 'false','isActive' : 'false'};
    $scope.types = types;
    $scope.fieldValues = {};

    $scope.cleanValue = function(){
        $scope.fieldValues = {};
    };


    $scope.save = function () {
        if ($scope.item.label === "" || $scope.item.type == null) {
            alert("Label and Type not filled");
            return;
        }
        if ($scope.fieldValues != null){
            var values = _.map($scope.fieldValues, function(element){
                return {'value' : element}
            });
            $scope.item.fieldValues = values;
        }
        $http.post('/api/v1/field', $scope.item).
            success(function (data, status, headers, config) {
                $location.path('/fields');
            }).
            error(function (data, status, headers, config) {
                $location.path('/fields');
            });
    };
}]);

app.controller("FieldEditController", ["$scope", "$routeParams", "$location", "$http", function ($scope, $routeParams, $location, $http) {
    $scope.types = types;
    $scope.item = null;

    $scope.addOption = function(){
      $scope.item.fieldValues.push({'value' : ""});
    };

    $scope.deleteOption = function(id){
        console.log("id" + id);
        $scope.item.fieldValues = _.reject($scope.item.fieldValues, function(element){
            return element.id == id;
        });
        $http.delete('/api/v1/deleteFieldOption' + id).success(function() {
            console.log("ok");
    });
    };

    $http.get('/api/v1/field/' + $routeParams.id).success(function (data, status, headers, config) {
        if (status > 200) {
            $location.path("/fields");
        }
        $scope.item = data;
    }).error(function (data, status, headers, config) {
        $location.path("/fields");
    });
    $scope.save = function () {
        $http.put('/api/v1/field/' + $scope.item.id, $scope.item).
            success(function (data, status, headers, config) {
                $location.path('/fields');
            }).
            error(function (data, status, headers, config) {
                $location.path('/fields');
            });
    }
}]);

app.controller("ResponsesCollectionController", ["$scope", "$location", "$http", function ($scope, $location, $http) {

    $scope.fields = [];
    $scope.form = [];

    $scope.updateCheckbox = function ($event, fieldId, valueId) {
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');

        var answer = {'field': {'id': parseInt(fieldId)}, 'answer': {'id': parseInt(valueId)}}

        if (checkbox.checked) {
            $scope.form.push(answer);
        } else {
            $scope.form = _.reject($scope.form, function (element) {
                return element.answer.id === valueId;
            });
        }
    };

    $scope.isSelected = function (valueId) {

        var obj = _.find($scope.form, function (element) {
            if (!_.has(element.answer, "id")) {
                return false;
            }
            return element.answer.id == valueId
        });
        return !_.isUndefined(obj);
    };

    $scope.updateRadio = function (radioValue) {
        $scope.form = _.reject($scope.form, function (element) {
            return element.field.id === radioValue.field.id;
        });
        $scope.form.push(radioValue);
    };

    $scope.submit = function () {
        $http.post('/api/v1/form', $scope.form);
        $location.path('/success');
    };

    $scope.reset = function () {
        $scope.form = [];
    };

    $http.get("/api/v1/field").success(function (data, status, headers, config) {
        $scope.fields = data;
    });
}]);


app.controller("ResponsesController", ["$scope", "$location", "$http", "$websocket", "$timeout", function ($scope, $location, $http, $websocket,$timeout) {
    $scope.columns = [];
    $scope.formData = [];

    $http.get("/api/v1/form").success(function (data, status, headers, config) {
        if (status > 200) {
            console.log("No content");
        } else {
            $scope.formData = data;
        }
    });

    $http.get("/api/v1/field/short").success(function (data, status, headers, config) {
        if (status > 200) {
            console.log("No content");
        } else {
            $scope.columns = data;
        }
    });

    var ws = $websocket.$new({
        url: 'ws://' + $location.host() + ':' + $location.port() + '/responseWS'
    });

    ws.$on('$message', function (message) {
        $scope.$apply(function(){
            $scope.formData.push(message);
        });
    });

    $scope.getColumnValue = function (responses, columnId) {
        var answers = _.filter(responses, function (element) {
            return element.field.id == columnId;
        });
        var answersArray = _.toArray(answers);
        var placeholder = "";
        if (_.isEmpty(answersArray)) {
            placeholder = "N/A";
        } else if (answersArray.length == 1) {
            placeholder = answersArray[0].answer.value;
        } else {
            _.each(answersArray, function (element) {
                placeholder = placeholder + element.answer.value + ";";
            });
        }
        return placeholder;
    };

}]);


app.controller("AdminHeaderController", ["$scope", "$http", "$location", "$websocket", function ($scope, $http, $location, $websocket) {
    $scope.count = 0;

    $http.get('/api/v1/formcount').success(function (data, status) {
            $scope.count = parseInt(data);
    });

    var ws = $websocket.$new({
        url: 'ws://' + $location.host() + ':' + $location.port() + '/headerWS',
        reconnect: true
    });

    ws.$on('$message', function (message) {
            $scope.$apply(function () {
                $scope.count++;
            });
        });
}]);

app.directive('customTextSave', function () {
    return function ($scope, element, attrs) {
        var index = $scope.form.length;
        $scope.form[index] = {'field': {'id': parseInt($scope.field.id)}, 'answer': null};
        $scope.$watch(attrs.ngModel, function (value) {
            if (value === "") {
                value = null;
            }
            $scope.form[index] = {'field': {'id': parseInt($scope.field.id)}, 'answer': {'value': value}};
        });
    }
});

app.directive('customSelectSave', function () {
    return function ($scope, element, attrs) {
        var index = $scope.form.length;
        $scope.form[index] = {'field': {'id': parseInt($scope.field.id)}, 'answer': null};
        $scope.$watch(attrs.ngModel, function (value) {
            $scope.form[index] = {'field': {'id': parseInt($scope.field.id)}, 'answer': {'id': parseInt(value)}};
        });
    }
});


var types = ['SingleLineText', 'Textarea', 'RadioButton', 'CheckBox', 'Combobox'];