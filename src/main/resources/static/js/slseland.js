function spinRoulette() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');

    const token = tokenMeta ? tokenMeta.getAttribute('content') : null;
    const header = headerMeta ? headerMeta.getAttribute('content') : null;

    const headers = header && token ? { [header]: token } : {}; // csrf 없으면 그냥 빈 headers

    fetch("/api/roulette/spin", {
        method: "POST",
        headers: headers
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
function closeResultModal() {
    document.getElementById("roulette-result-modal").style.display = 'none';
}
