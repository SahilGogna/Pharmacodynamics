var start = new Date().getTime();
		var end = null;

		function uuidv4() {
			return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
				var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
				return v.toString(16);
			});
		}

		var sessionId = uuidv4();

		function resetPosition() {
			if (end == null) {
				start = new Date().getTime();
				end = start;
			} else {
				end = new Date().getTime();
				fetch("http://localhost:8888/api/report?sessionId=" + sessionId + "&time=" + (end - start));
				start = end;
			}

			var redSquare = document.getElementById("box-red");
			var greenSquare = document.getElementById("box-green");
			var blueSquare = document.getElementById("box-blue");
			var yellowSquare = document.getElementById("box-yellow");
			var yellowCircle = document.getElementById("circle-yellow");

			var random = Math.random() * 100;
			redSquare.style.top = Math.round(random) + "%";
			random = Math.random() * 100;
			redSquare.style.left = Math.round(random) + "%";

			greenSquare.style.top = Math.round(Math.random() * 100) + "%";
			greenSquare.style.left = Math.round(Math.random() * 100) + "%";

			blueSquare.style.top = Math.round(Math.random() * 100) + "%";
			blueSquare.style.left = Math.round(Math.random() * 100) + "%";

			yellowSquare.style.top = Math.round(Math.random() * 100) + "%";
			yellowSquare.style.left = Math.round(Math.random() * 100) + "%";

			yellowCircle.style.top = Math.round(Math.random() * 100) + "%";
			yellowCircle.style.left = Math.round(Math.random() * 100) + "%";
		}

		window.onload = function () {
			resetPosition();
			var redSquare = document.getElementById("box-red");
			redSquare.onclick = resetPosition;
		}