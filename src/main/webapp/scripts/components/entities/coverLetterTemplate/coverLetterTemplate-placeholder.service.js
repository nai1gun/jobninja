'use strict';

angular.module('jhtestApp')
    .factory('CoverLetterTemplatePlaceholder', ['$translate', '$q', function ($translate, $q) {
        var getAll = function() {
            return $q(function(resolve) {
                var prefix = 'jhtestApp.coverLetterTemplate.templatePlaceholder.';
                var translateAttrs = [prefix + 'position.placeholder', prefix + 'position.title',
                    prefix + 'company.placeholder', prefix + 'company.title',
                    prefix + 'location.placeholder', prefix + 'location.title'];
                $translate(translateAttrs).then(function(result) {
                    resolve(
                        {
                            position: {
                                placeholder: result[translateAttrs[0]],
                                title: result[translateAttrs[1]]
                            },
                            company: {
                                placeholder: result[translateAttrs[2]],
                                title: result[translateAttrs[3]]
                            },
                            location: {
                                placeholder: result[translateAttrs[4]],
                                title: result[translateAttrs[5]]
                            }
                        }
                    );
                });
            });
        };
        return {
            getAll: getAll,
            resolve: function(text, position) {
                return $q(function(resolve) {
                    getAll().then(function(placeholders) {
                        var positionPattern = pattern(placeholders.position);
                        var companyPattern = pattern(placeholders.company);
                        var locationPattern = pattern(placeholders.location);
                        text = text.replace(positionPattern, position.name);
                        text = text.replace(companyPattern, position.company);
                        text = text.replace(locationPattern, position.location);
                        resolve(text);
                        function pattern(placeholderObj) {
                            return new RegExp('\\[' + placeholderObj.placeholder + '\\]', 'g');
                        }
                    });
                })
            }
        };
    }]);
