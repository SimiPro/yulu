// Start the main app logic.
require([
        'angular',
        'angular-route',
        'angular-resource',
        'require',
        '/web/vassets/javascripts/web-app.js',
        '/users/vassets/javascripts/user-module.js'
    ],
    function(angular,SignUpController) {
        var appName = 'yulu'
        angular.module('yulu', ['ngResource', 'ngRoute', 'web', 'user'])
            .factory('navigation', function($resource) {
                return $resource('navigation');
            })
            .config([
                '$routeProvider',
                function($routeProvider) {
                    $routeProvider.when('/', {
                        templateUrl: 'web/vassets/partials/welcome.tpl.html'
                    });
                    return $routeProvider.otherwise({
                        redirectTo: '/'
                    });
                }
            ]);
        return angular.bootstrap(document, [appName]);
    });
