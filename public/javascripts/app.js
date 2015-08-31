var app = angular.module('app', ['ngRoute', 'ngWebsocket', 'ngCookies', 'facebook'])
    .constant("apiUrl", "http://localhost:9000\:9000/api/v1")
    .config(["$routeProvider", function ($routeProvider) {
        return $routeProvider.when("/", {
            redirectTo: "/landing"
        }).when("/landing", {
            templateUrl: "/views/landing",
            controller: "LandingController"
        }).when("/pills", {
            templateUrl: "/views/pillList",
            controller: "PillListController"
        }).when("/create", {
            templateUrl: "/views/createPill",
            controller: "CreatePillController"
        }).when("/medication/:id", {
            templateUrl: "/views/editPill",
            controller: "PillEditController"
        }).when("/success",{
            templateUrl: "/views/success"
        }).otherwise({
            redirectTo: "/"
        });
    }
    ]).config([
        "$locationProvider", "FacebookProvider", function ($locationProvider, FacebookProvider) {
            $locationProvider.html5Mode(true).hashPrefix("!");
            var myAppId = '1461597530818412';
            FacebookProvider.setAppId('myAppId');
            FacebookProvider.init(myAppId);
        }
    ]);

app.controller("navController", ["$scope", "$cookieStore", "$location", "$http", "$route", "Facebook", function ($scope, $cookieStore, $location, $http, $route, Facebook) {

    $scope.singOut = function() {
        $cookieStore.remove("id");
        $location.path("/");
    };

}]);

app.controller("LandingController", ["$scope", "$cookieStore", "$location", "$http", "$route", "Facebook", function ($scope, $cookieStore, $location, $http, $route, Facebook) {

    var token = $cookieStore.get("id");
    if (token) {
        console.log(token);
        $location.path("/pills")
    }

    $scope.user = {};

    var userIsConnected = false;

    Facebook.getLoginStatus(function(response) {
        if (response.status == 'connected') {
            userIsConnected = true;
        }
    });

    $scope.IntentLogin = function() {
        if(!userIsConnected) {
            $scope.login();
        } else {
            $scope.me();
        }
    };

    $scope.login = function() {
        Facebook.login(function(response) {
            if (response.status == 'connected') {
                $scope.logged = true;
                $scope.me();
            }

        });
    };

    $scope.me = function() {
        Facebook.api('/me', function(response) {
            $scope.$apply(function() {
                $scope.user = response;
                $cookieStore.put("id",$scope.user.id);
                $cookieStore.put("name",$scope.user.first_name);
                $http.post('/api/v1/user', $scope.user).
                    success(function (data, status, headers, config) {
                        $location.path('/fields');
                    }).
                    error(function (data, status, headers, config) {
                        $location.path('/fields');
                    });
            });



        });
    };
}]);

app.controller("PillListController", ["$scope", "$cookieStore", "$location", "$http", "$route", function ($scope, $cookieStore, $location, $http, $route) {
    validate($location, $cookieStore);
    $scope.fields = [];
    var load = function(){
        $http.get("/api/v1/user/" + $cookieStore.get("id") + "/medication").
            success(function (data, status, headers, config) {
                $scope.fields = data;
            });
    };
    load();

    $scope.addPill = function () {
        $location.path('/create');
        console.log("create")
    };
    $scope.delPill = function (field) {
        $http.delete('/api/v1/medication/' + field.id).success($scope.$apply(load()));
    }
}]);

app.controller("CreatePillController", ["$scope", "$cookieStore", "$location", "$http", function ($scope, $cookieStore, $location, $http) {
    validate($location, $cookieStore);
    $scope.item = {};
    $scope.item.title = "";
    $scope.item.count = null;

    $scope.cleanValue = function(){
        $scope.item = {};
    };


    $scope.save = function () {
        if ($scope.item.title === "" || $scope.item.count == null) {
            alert("Title and Count not filled");
            return;
        }
        if ($scope.fieldValues != null){
            var values = _.map($scope.fieldValues, function(element){
                return {'value' : element}
            });
            $scope.item.fieldValues = values;
        }
        $http.post('/api/v1/user/' + $cookieStore.get("id") + "/medication", $scope.item).
            success(function (data, status, headers, config) {
                $location.path('/');
            }).
            error(function (data, status, headers, config) {
                $location.path('/');
            });
    };
}]);

app.controller("PillEditController", ["$scope", "$routeParams", "$location", "$http", function ($scope, $routeParams, $location, $http) {
    validate($location, $cookieStore);
    $scope.item = null;

    $scope.addOption = function(){
      $scope.item.fieldValues.push({'value' : ""});
    };

    $http.get('/api/v1/medication/' + $routeParams.id).success(function (data, status, headers, config) {
        if (status > 200) {
            $location.path("/");
        }
        $scope.item = data;
    }).error(function (data, status, headers, config) {
        $location.path("/");
    });
    $scope.save = function () {
        $http.put('/api/v1/medication/' + $scope.item.id, $scope.item).
            success(function (data, status, headers, config) {
                $location.path('/');
            }).
            error(function (data, status, headers, config) {
                $location.path('/');
            });
    }
}]);

function validate($location, $cookieStore){
    var token = $cookieStore.get("id");
    if (!token) {
        console.log(token);
        $location.path("/landing")
    }
}