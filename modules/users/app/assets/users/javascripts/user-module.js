 'use strict'

  define(['angular'], function(angular) {
    var user = angular.module('user', ['ngResource','ngRoute','web']);


     user.factory('navigation', function($resource){
          return $resource('navigation');
        });

        user.config([
          '$routeProvider', function($routeProvider) {
            $routeProvider.when('/home', {
              templateUrl: 'users/vassets/partials/home.tpl.html'
            });
            $routeProvider.when('login', {
              templateUrl: 'users/vassets/partials/login.tpl.html'
            });
            $routeProvider.when('signup',{
              templateUrl: 'users/vassets/partials/signup.tpl.html'
            });
            return $routeProvider.otherwise({
              redirectTo: '/home'
            });
          }
        ]);
});