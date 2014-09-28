(function(define) {
    'use strict';    
    define(['angular'], function(angular)     {
        var SignUpController = function($scope, $http)         {
            $scope.form = $scope.form || {}
            $scope.hello = "hello";


            $scope.sendEmail = function() {
                console.log("sendEmail to: ");
                console.log($scope)
                console.log($scope.form);
                $http.post('users/signup', $scope.form);

            };
            console.log($scope);
                         // Publish instance with simple login() and logout() APIs
            return {            
                login: function() {
                    console.log("login");
                },
                logout: function() {
                    console.log("logout");
                }            
            };        
        };          // Publish the constructor/construction array
                
        return ["$scope", "$http", SignUpController];    
    }); 
}(define));

/*
(function() 
{
 
    define( function() 
    {
        var SignUpController = function( )
        {
            // Publish instance with simple login() and logout() APIs
            return {
            login : function( ) { console.log("login"); },
            logout : function() { console.log("logout"); }
            };
        };
 
        // Publish the constructor/construction array
        return SignUpController;
    });
 
}());
*/
