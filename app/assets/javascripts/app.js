// Start the main app logic.
requirejs([
  'angular',
  'angular-route',
  'angular-resource',
  '/web/vassets/javascripts/web-app.js'],
function   (angular) {

    var yulu = angular.module('yulu', ['ngResource','ngRoute','web']);
    yulu.factory('navigation', function($resource){
      return $resource('navigation');
    });

    yulu.config([
      '$routeProvider', function($routeProvider) {
        $routeProvider.when('/', {
          templateUrl: 'web/vassets/partials/welcome.tpl.html'
        });
        return $routeProvider.otherwise({
          redirectTo: '/'
        });
      }
    ]);
    
    return angular.bootstrap(document, ['yulu']);

});