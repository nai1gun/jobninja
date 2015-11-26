'use strict';

angular.module('jhtestApp').controller('PositionCvDialogController',
    ['$scope', '$rootScope', '$modalInstance', '$state', 'Position', 'UserCv', 'Upload', 'PositionUtils',
        function($scope, $rootScope, $modalInstance, $state, Position, UserCv, Upload, PositionUtils) {

        $scope.position = $state.$current.parent.data.position;
        $scope.cvs = [];
        $scope.cvFile = null;

        $scope.ok = function() {
            $modalInstance.dismiss('ok');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.$watch('cvFile', function(newValue, oldValue) {
            if (newValue != null && newValue.length) {
                newValue.upload = Upload.upload({
                    url: '/api/users/current/cv',
                    file: newValue
                });

                newValue.upload.then(function (response) {
                    $scope.cvs.push(response.data);
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                });
            }
        });

        $scope.isCvChosen = function(cv) {
            return $scope.position != undefined && $scope.position.cv != undefined && $scope.position.cv.filePath == cv.filePath;
        };

        $scope.chooseCv = function(cv) {
            $scope.position.cv = cv;
        };

        $scope.s3url = PositionUtils.s3url;

        $scope.fileName = PositionUtils.fileName;

        var loadCvs = function() {
            UserCv.query(function(result){
                $scope.cvs = result;
            });
        };

        loadCvs();

}]);
