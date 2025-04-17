function closeModal() {

        document.getElementById("overlay").style.display = 'none';
        document.getElementById("champion-modal").style.display = 'none';
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll('.champion-card').forEach(card => {
            card.addEventListener('click', (evt) => {
                const data = evt.currentTarget.dataset;
                document.getElementById("modal-name").innerText = data.name;
                document.getElementById("modal-title").innerText = data.title;
                document.getElementById("modal-blurb").innerText = data.blurb;
                document.getElementById("modal-img").src = data.imageUrl;
                document.getElementById("modal-champion-id").value = data.id;
                if(document.getElementById("modal-price")) {

                    document.getElementById("modal-price").innerText = parseInt(data.price).toLocaleString() + ' SLSE';

                }


                document.getElementById("overlay").style.display = 'block';
                document.getElementById("champion-modal").style.display = 'block';
                loadComments();
            });
        });

        const actionBtn = document.getElementById("action-btn");
        if (actionBtn) {
            actionBtn.onclick = function () {
                const championId = document.getElementById("modal-champion-id").value;

                fetch("/champion/buy?champion-id=" + encodeURIComponent(championId), {
                    method: "get"
                }).then(res => res.text())
                    .then(result => {
                        if (result === 'failed') {
                            window.alert("오류가 생겼습니다");
                        } else if (result === 'success') {
                            window.alert("구매가 완료되었습니다");
                            closeModal();
                        }
                    });
            };
        }
    });
    function loadComments() {
        const championId = document.getElementById("modal-champion-id").value;
        fetch(`/champion/comments?champion-id=${championId}`)
            .then(res => res.json())
            .then(data => {
                const list = document.getElementById("comment-list");
                list.innerHTML = "";

                if (data.length === 0) {
                    list.innerHTML = "<p>댓글이 아직 없습니다.</p>";
                    return;
                }

                data.forEach(comment => {
                    const isMine = comment.isMine;
                    const div = document.createElement("div");
                    div.style.borderBottom = "1px solid #ddd";
                    div.style.padding = "10px 0";
                    div.innerHTML = `
                        <b>${comment.nickname || '익명'}</b>
                        ${isMine ? `
                            <button onclick="editComment(${comment.id}, '${comment.content}')">✎</button>
                            <button onclick="deleteComment(${comment.id})">🗑</button>` : ''}
                        <br>
                        <div id="comment-content-${comment.id}">${comment.content}</div>
                        <small class="custom-style-2">${comment.createdAt}</small>
                    `;
                    list.appendChild(div);
                });
            });
    }

    function submitComment() {
        const content = document.getElementById("comment-content").value;
        const championId = document.getElementById("modal-champion-id").value;

        if (!content.trim()) {
            alert("댓글을 입력하세요.");
            return;
        }

        fetch('/champion/post', {
            method: 'POST',

            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams({
                championId: championId,
                content: content
            })

        }).then(res => res.text())
            .then(result => {
                if (result === 'OK' || result === 'YYY') {
                    document.getElementById("comment-content").value = '';
                    loadComments();
                } else {
                    alert("댓글 등록 실패: " + result);
                }
            });
    }

    function editComment(id, content) {
        const container = document.getElementById(`comment-content-${id}`);
        container.innerHTML = `
            <textarea id="edit-content-${id}" class="custom-style-13">${content}</textarea>
            <button onclick="submitEdit(${id})">수정 완료</button>
        `;
    }

    function submitEdit(id) {
        const content = document.getElementById(`edit-content-${id}`).value;

        fetch('/champion/update-post', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                'champion-post-id': id,
                content
            })
        }).then(res => res.text())
            .then(result => {
                if (result === 'OK') {
                    loadComments();
                } else {
                    alert("수정 실패");
                }
            });
    }

    function deleteComment(id) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        fetch(`/champion/delete-post?champion-post-id=${id}`, {
            method: "POST"
        }).then(res => res.text())
            .then(result => {
                if (result === 'OK') {
                    loadComments();
                } else {
                    alert("삭제 실패: " + result);
                }
            });
    }

    function toggleCategory() {
        const categoryBox = document.getElementById('category-box');
        const championList = document.getElementById('champion-list');
        const toggleBtn = document.getElementById('toggle-btn');

        if (categoryBox.classList.contains('collapsed')) {
            categoryBox.classList.remove('collapsed');
            championList.style.gridTemplateColumns = 'repeat(5, 1fr)';
            toggleBtn.textContent = '◀';
        } else {
            categoryBox.classList.add('collapsed');
            championList.style.gridTemplateColumns = 'repeat(6, 1fr)';
            toggleBtn.textContent = '▶';
        }
    }

    const slide = document.getElementById('banner-slide');
    const totalSlide = slide.children.length;
    let currentIndex = 0;

    function showSlide(index) {
        slide.style.transform = 'translateX(' + (-index * 100) + '%)';
    }

    window.prevSlide = function () {
        currentIndex = (currentIndex - 1 + totalSlide) % totalSlide;
        showSlide(currentIndex);
    };

    window.nextSlide = function () {
        currentIndex = (currentIndex + 1) % totalSlide;
        showSlide(currentIndex);

    }

    function applySort() {
        const value = document.getElementById("sort-option").value;
        const [type, sort] = value.split("-");

        fetch(`/sort?type=${type}&sort=${sort}`)
            .then(res => res.json())
            .then(data => {
                const listBox = document.getElementById('champion-list');
                listBox.innerHTML = "";

                data.forEach(one => {
                    const card = document.createElement("div");
                    card.className = "champion-card";
                    card.dataset.id = one.id;
                    card.dataset.name = one.name;
                    card.dataset.title = one.title;
                    card.dataset.blurb = one.blurb;
                    card.dataset.imageUrl = one.imageUrl;
                    card.dataset.price = one.price;

                    card.innerHTML = `
                    <img src="${one.imageUrl}">
                    <div class="custom-style-14">${one.name}</div>
                `;

                    card.addEventListener('click', (evt) => {
                        const data = evt.currentTarget.dataset;
                        document.getElementById("modal-name").innerText = data.name;
                        document.getElementById("modal-title").innerText = data.title;
                        document.getElementById("modal-blurb").innerText = data.blurb;
                        document.getElementById("modal-img").src = data.imageUrl;
                        document.getElementById("modal-champion-id").value = data.id;
                        if(document.getElementById("modal-price")) {
                            document.getElementById("modal-price").innerText = parseInt(data.price).toLocaleString() + ' SLSE';
                        }
                        document.getElementById("overlay").style.display = 'block';
                        document.getElementById("champion-modal").style.display = 'block';
                        loadComments();
                    });

                    listBox.appendChild(card);
                });
            });
    }