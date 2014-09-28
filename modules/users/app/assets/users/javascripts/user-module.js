(function(define, angular) {
        'use strict';

        define(['angular','vassets/users/javascripts/controllers/SignUpController.js'],
            function(angular, SignUpController) {
                var moduleName = "user"
                angular.module(moduleName, ['ngResource', 'ngRoute', 'web'])
                    .controller("SignUpController", SignUpController)
                    .factory('navigation', function($resource) {
                        return $resource('navigation');
                    })
                    .config(function($locationProvider, $routeProvider) {
                        $routeProvider
                            .when('/home', {
                                templateUrl: 'users/vassets/partials/home.tpl.html'
                            })
                            .when('/login', {
                                templateUrl: 'users/vassets/partials/login.tpl.html'
                            })
                            .when('/signup', {
                                templateUrl: '/users/vassets/partials/signup-startup.tpl.html'
                            })
                            .when('signup/:token', {
                                templateUrl: '/users/vassets/partials/signup.tpl.html'
                            })
                            .otherwise({
                                redirectTo: '/home'
                            });
                    });
            return moduleName;
            });
    }(define));
