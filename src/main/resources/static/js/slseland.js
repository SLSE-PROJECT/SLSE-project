function spinRoulette() {
    fetch("/api/roulette/spin", {
        method: "POST"
    })
        .then(res => res.json())
        .then(data => {
            document.getElementById("result-name").innerText = data.rewardName;

            const img = document.getElementById("result-image");
            if (data.rewardImage) {
                img.src = data.rewardImage;
                img.style.display = 'block';
            } else {
                img.style.display = 'none';
            }

            document.getElementById("result-description").innerText = data.rewardDescription || '';
            document.getElementById("roulette-result-modal").style.display = 'block';
        })
        .catch(err => {
            alert("룰렛 오류: " + err.message);
        });
}