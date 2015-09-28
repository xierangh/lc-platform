(function() {
	var app = angular.module("com.lc.platform.xzqh", []);
	app.controller('XzqhController', function($scope, $http) {
		var editNodeData;
		var xzqhController = this;
		var zTreeOnClick = function(event, treeId, treeNode) {
			$scope.xzqh = treeNode.data;
			editNodeData = treeNode.data;
			$scope.$apply();// $watch,$digest
		};

		$scope.loadTreeDataHandler = function(event) {
			if (event.keyCode == 13) {
				$scope.loadTreeData();
			}
		};
		$scope.resetForm = function() {

		};

		$scope.loadTreeData = function() {
			$.showLoading();
			angular.element("#xzqhTree").html("");
			$http({
				url : contextPath + "/xzqhs/search",
				params : {
					content : $scope.content
				}
			}).success(function(data, status, headers, config) {
				$.fn.zTree.init($("#xzqhTree"), {
					data : {
						simpleData : {
							enable : true
						}
					},
					view : {
						nameIsHTML : true
					},
					async : {
						enable : true,
						url : contextPath + "/xzqhs/childs",
						autoParam : [ "id" ]
					},
					callback : {
						onClick : zTreeOnClick
					}
				}, data);
				$.hideLoading();
			})
		}
	});
})();
