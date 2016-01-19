'use strict';

angular.module('jhtestApp')
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('social-register', {
                parent: 'account',
                url: '/social-register/:provider?{success:boolean}',
                data: {
                    roles: [],
                    pageTitle: 'social.register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/social/social-register.html',
                        controller: 'SocialRegisterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('social');
                        return $translate.refresh();
                    }]
                }
            });
    }]);
