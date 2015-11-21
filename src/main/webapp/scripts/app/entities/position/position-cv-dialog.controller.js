'use strict';

angular.module('jhtestApp').controller('PositionCvDialogController',
    ['$scope', '$rootScope', '$modalInstance', '$state', 'Position', 'UserCv', 'Upload', 'S3_PREFIX',
        function($scope, $rootScope, $modalInstance, $state, Position, UserCv, Upload, S3_PREFIX) {

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
            return $scope.position.cv && $scope.position.cv.filePath == cv.filePath;
        };

        $scope.chooseCv = function(cv) {
            $scope.position.cv = cv;
        };

        $scope.s3url = function(cv) {
            return S3_PREFIX + cv.filePath;
        };

        var loadCvs = function() {
            UserCv.query(function(result){
                $scope.cvs = result;
            });
        };

        loadCvs();

}]);
