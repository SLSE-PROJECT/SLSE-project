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

function spinRoulette() {
    const roulette = document.getElementById("roulette");
    const slotCount = 8;
    const slotWidth = 150; // .slot의 최소 너비
    const spinRounds = 5; // 몇 바퀴 돌릴지

    // 랜덤 인덱스 (0~7)
    const randomIndex = Math.floor(Math.random() * slotCount);

    // 총 이동 거리
    const totalOffset = (slotCount * spinRounds + randomIndex) * slotWidth;

    roulette.style.transition = "transform 3s cubic-bezier(0.25, 0.1, 0.25, 1)";
    roulette.style.transform = `translateX(-${totalOffset}px)`;

    // 결과 처리 (예: 이름 출력)
    setTimeout(() => {
        const rewards = ["🎁 보상1", "💰 보상2", "🧧 보상3", "🎟 보상4", "🔥 보상5", "💎 보상6", "🎉 보상7", "🪙 보상8"];
        alert("당첨: " + rewards[randomIndex]);
    }, 3000);
}