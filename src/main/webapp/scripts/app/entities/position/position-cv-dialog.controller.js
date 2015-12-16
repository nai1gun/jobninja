'use strict';

angular.module('jhtestApp').controller('PositionCvDialogController',
    ['$scope', '$rootScope', '$modalInstance', '$state', 'Position', 'UserCv', 'Upload', 'PositionUtils', 'DateUtils',
        function($scope, $rootScope, $modalInstance, $state, Position, UserCv, Upload, PositionUtils, DateUtils) {

        /*
          Not sure why but this depends on the the url page we opened page
            ( .../ or .../edit or .../edit/cv )
        */
        $scope.position = $state.$current.parent.parent.data.position || $state.$current.parent.data.position ||
            $state.$current.data.position;
        $scope.cvs = [];
        $scope.cvFile = null;
        $scope.uploading = false;

        var $cv = $scope.position? $scope.position.cv: null;

        $scope.ok = function() {
            $modalInstance.dismiss('ok');
        };

        $scope.cancel = function() {
            $scope.position.cv = $cv;
            $modalInstance.dismiss('cancel');
        };

        $scope.$watch('cvFile', function(newValue, oldValue) {
            if (newValue != null && newValue.length) {
                $scope.uploading = true;
                newValue.upload = Upload.upload({
                    url: '/api/users/current/cv',
                    file: newValue,
                    headers: {
                        "created": DateUtils.convertLocaleDateToServer(new Date()).toISOString()
                    }
                });

                newValue.upload.then(function (response) {
                    $scope.uploading = false;
                    var cv = response.data;
                    $scope.cvs.push(cv);
                    $scope.position.cv = cv;
                    $scope.ok();
                }, function (response) {
                    $scope.uploading = false;
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                });
            }
        });

        $scope.isCvChosen = function(cv) {
            return $scope.position != undefined && $scope.position.cv != undefined && $scope.position.cv.filePath == cv.filePath;
        };

        $scope.chooseCv = function(cv) {
            if ($scope.position.cv == null || $scope.position.cv.filePath != cv.filePath) {
                $scope.position.cv = cv;
            } else {
                $scope.position.cv = null;
            }
        };

        $scope.deleteCv = function(cv) {
            if (cv.filePath == $scope.position.cv.filePath) {
                $scope.position.cv = null;
            }
            UserCv.delete(cv, function() {
                var index = $scope.cvs.indexOf(cv);
                $scope.cvs.splice(index, 1);
            });
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
