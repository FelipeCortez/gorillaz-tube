const start = () => {
  const socket = new WebSocket('ws://127.0.0.1:5557/');

  socket.addEventListener('message', (event) => {
    if (event.data.trim() == "reload") {
      location.reload();
    }
  });

  socket.addEventListener('close', (event) => {
    setTimeout(start, 1000)
  });
}

start();
