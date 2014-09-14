'use strict'

 define(['angular'], function(angular) {
 	var web = angular.module('web',['ngResource', 'ngRoute']);

 	web.directive('yuluHeader', function() {
      return {
        restrict: 'E',
        scope: {
          h1: '@',
          h2: '@',
          lead: '@',
          subtext: '@',
          navModule: '@',
          navService: '@'
        },
        templateUrl: 'web/vassets/partials/header.tpl.html'
      };
    });
    return web.directive('yuluNav', function() {
      return {
        restrict: 'E',
        scope: {
          module: '@',
          service: '@'
        },
        templateUrl: 'web/vassets/partials/navigation.tpl.html',
        link: function($scope) {
          return $scope.data = angular.injector([$scope.module]).get($scope.service).get(function() {
            return $scope.$apply();
          });
        }
      };
    });
 });