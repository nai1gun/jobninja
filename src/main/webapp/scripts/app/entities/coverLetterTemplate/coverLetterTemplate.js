'use strict';

angular.module('jhtestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('coverLetterTemplate', {
                parent: 'entity',
                url: '/coverLetterTemplates',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.coverLetterTemplate.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplates.html',
                        controller: 'CoverLetterTemplateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coverLetterTemplate');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('coverLetterTemplate.new', {
                parent: 'entity',
                url: '/coverLetterTemplate/new',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.coverLetterTemplate.detail.title',
                    editing: true
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplate-detail.html',
                        controller: 'CoverLetterTemplateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coverLetterTemplate');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {};
                    }
                }
            })
            .state('coverLetterTemplate.detail', {
                parent: 'entity',
                url: '/coverLetterTemplate/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.coverLetterTemplate.detail.title',
                    editing: false
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplate-detail.html',
                        controller: 'CoverLetterTemplateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coverLetterTemplate');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CoverLetterTemplate', function($stateParams, CoverLetterTemplate) {
                        return CoverLetterTemplate.get({id : $stateParams.id});
                    }]
                }
            })
            .state('coverLetterTemplate.edit', {
                parent: 'coverLetterTemplate.detail',
                url: '/edit',
                data: {
                    roles: ['ROLE_USER'],
                    editing: true
                }
            });
    });
