const list = document.getElementById('list');
const searchForm = document.getElementById('searchForm');
const loginButton = document.getElementById('loginButton');
const loginContainer = document.getElementById('loginContainer');
const detailContainer = document.getElementById('detailContainer');
detailContainer.show = (placeObject) => {
    detailContainer.querySelectorAll('[rel="title"]').forEach(x => x.innerText = placeObject['name']);
    detailContainer.querySelector('[rel="addressText"]').innerText = `${placeObject['addressPrimary']}${placeObject['addressSecondary'] ? `\n${placeObject['addressSecondary']}` : ''}`;
    const openFrom = new Date(placeObject['openFrom']);
    const openTo = new Date(placeObject['openTo']);
    detailContainer.querySelector('[rel="openText"]').innerText = `${openFrom.getHours() < 10 ? '0' : ''}${openFrom.getHours()}:${openFrom.getMinutes() < 10 ? '0' : ''}${openFrom.getMinutes()} ~ ${openTo.getHours() < 10 ? '0' : ''}${openTo.getHours()}:${openTo.getMinutes() < 10 ? '0' : ''}${openTo.getMinutes()}`;
    const contact = `${placeObject['contactFirst']}-${placeObject['contactSecond']}-${placeObject['contactThird']}`
    detailContainer.querySelector('[rel="contactText"]').innerText = contact;
    detailContainer.querySelector('[rel="contactText"]').setAttribute('href', `tel:${contact}`);
    const homepageTextElement = detailContainer.querySelector('[rel="homepageText"]');
    if (placeObject['homepage']) {
        homepageTextElement.innerText = placeObject['homepage'];
        homepageTextElement.setAttribute('href', placeObject['homepage']);
        homepageTextElement.parentElement.parentElement.classList.remove('hidden');
    } else {
        homepageTextElement.parentElement.parentElement.classList.add('hidden');
    }
    detailContainer.querySelector('[rel="descriptionText"]').innerText = placeObject['description'];
    reviewForm['placeIndex'].value = placeObject['index'];
    detailContainer.classList.add('visible');
};
detailContainer.hide = () => detailContainer.classList.remove('visible');
detailContainer.querySelector('[rel="closeButton"]').addEventListener('click', () => {
    detailContainer.hide();
});
const reviewForm = document.getElementById('reviewForm');
const mapContainer = document.getElementById('mapContainer');

let mapObject;
let places = [];
const loadMap = (lat, lng) => {
    mapObject = new kakao.maps.Map(mapContainer, {
        center: new kakao.maps.LatLng(lat, lng),
        level: 3
    });
    kakao.maps.event.addListener(mapObject, 'dragend', () => {
        loadPlaces();
    });
    kakao.maps.event.addListener(mapObject, 'zoom_changed', () => {
        loadPlaces();
    });
    loadPlaces();
};
const loadPlaces = (ne, sw) => {
    if (!ne || !sw) {
        const bounds = mapObject.getBounds();
        ne = bounds.getNorthEast();
        sw = bounds.getSouthWest();
    }
    list.innerHTML = '';
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./data/place?minLat=${sw['Ma']}&minLng=${sw['La']}&maxLat=${ne['Ma']}&maxLng=${ne['La']}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const placeArray = JSON.parse(xhr.responseText);
                places = placeArray;
                for (const placeObject of placeArray) {
                    const position = new kakao.maps.LatLng(
                        placeObject['latitude'],
                        placeObject['longitude']);
                    const marker = new kakao.maps.Marker({
                        position: position,
                        clickable: true
                    });
                    kakao.maps.event.addListener(marker, 'click', () => {
                        detailContainer.show(placeObject);
                    });
                    marker.setMap(mapObject);

                    const openFrom = new Date(placeObject['openFrom']);
                    const placeHtml = `
                        <li class="item visible" rel="item">
                            <span class="info">
                                <span class="name-container">
                                    <span class="name" rel="name">${placeObject['name']}</span>
                                    <span class="category">${placeObject['categoryIndex']}</span>
                                </span>
                                <span class="rating-container">
                                    <span class="star-container">
                                        <i class="star filled fa-solid fa-star"></i>
                                        <i class="star filled fa-solid fa-star"></i>
                                        <i class="star filled fa-solid fa-star"></i>
                                        <i class="star filled fa-solid fa-star"></i>
                                        <i class="star fa-solid fa-star"></i>
                                    </span>
                                    <span class="score">4.1</span>
                                    <span class="count">7건</span>
                                    <span class="review-count">리뷰 13</span>
                                </span>
                                <span class="open-container">
                                    <span class="working">영업 전</span>
                                    <span class="hour">${openFrom.getHours() < 10 ? '0' : ''}${openFrom.getHours()}:${openFrom.getMinutes() < 10 ? '0' : ''}${openFrom.getMinutes()}에 영업 시작</span>
                                </span>
                                <span class="address">${placeObject['addressPrimary']} ${placeObject['addressSecondary'] ?? ''}</span>
                                <span class="contact">${placeObject['contactFirst']}-${placeObject['contactSecond']}-${placeObject['contactThird']}</span>
                            </span>
                            <img alt="" class="image" src="./data/placeImage?pi=${placeObject['index']}">
                        </li>`;
                    const placeElement = new DOMParser()
                        .parseFromString(placeHtml, 'text/html')
                        .querySelector('[rel="item"]');
                    placeElement.addEventListener('click', () => {
                        const latLng = new kakao.maps.LatLng(placeObject['latitude'], placeObject['longitude']);
                        mapObject.setCenter(latLng);
                        detailContainer.show(placeObject);
                    });
                    list.append(placeElement);
                }
            } else {

            }
        }
    };
    xhr.send();
};

navigator.geolocation.getCurrentPosition(e => {
    loadMap(e['coords']['latitude'], e['coords']['longitude']);
}, () => {
    loadMap(35.8715411, 128.601505);
});

searchForm['keyword'].addEventListener('input', () => {
    const keyword = searchForm['keyword'].value;
    const itemArray = Array.from(list.querySelectorAll(':scope > [rel="item"]'));
    for (let item of itemArray) {
        const name = item.querySelector('[rel="name"]').innerText;
        if (keyword === '' || name.indexOf(keyword) > -1) {
            item.classList.add('visible');
        } else {
            item.classList.remove('visible');
        }
    }
});


//https://developers.kakao.com 가서 카카오 로그인 활성화
// 맨 밑에 url -> http://localhost:8080/member/login 추가
// 동의항목 들어가서  닉네임 설정 -> 필수동의 누르고 밑에  동의목적 : 사용자 식별

// index.html 에서
//<div class="button-container">
//<a class="button" href="https://kauth.kakao.com/oauth/authorize?client_id=c2204b6ef796ea3923e04483a8e6a9c5&redirect_uri=http://localhost:8080/member/kakao&response_type=code" id="loginButton" target="_blank" >로그인</a>
//</div>
//client_id 뒤에는 REST API 키 붙여넣기
//redirect_uri 뒤에는 로그인 활성화할 때 맨 밑에 추가해준 url 이랑 같게 해줘야함

//로그인, 로그아웃
loginButton?.addEventListener('click', e => {
    e.preventDefault();
    loginContainer.classList.add('visible');
    window.open('https://kauth.kakao.com/oauth/authorize?client_id=c2204b6ef796ea3923e04483a8e6a9c5&redirect_uri=http://localhost:8080/member/kakao&response_type=code', '_blank', 'width=500; height=750'); //팝업 창 염
});

reviewForm.querySelector('[rel="imageSelectButton"]').addEventListener('click', e => {
    e.preventDefault();
    reviewForm['images'].click();
});

const reviewStarArray = Array.from(reviewForm
    .querySelector('[rel="starContainer"]')
    .querySelectorAll(':scope > .star'));
for (let i = 0; i < reviewStarArray.length; i++) {
    reviewStarArray[i].addEventListener('click', () => {
        reviewStarArray.forEach(x => x.classList.remove('selected'));
        for (let j = 0; j <= i; j++) {
            reviewStarArray[j].classList.add('selected');
        }
        reviewForm.querySelector('[rel="score"]').innerText = i + 1;
        reviewForm['score'].value = i + 1;
    });
}

reviewForm['images'].addEventListener('input', () => {
    const imageContainerElement = reviewForm.querySelector('[rel="imageContainer"]');
    imageContainerElement.querySelectorAll('img.image').forEach(x => x.remove());
    if (reviewForm['images'].files.length > 0) {
        reviewForm.querySelector('[rel="noImage"]').classList.add('hidden');
    } else {
        reviewForm.querySelector('[rel="noImage"]').classList.remove('hidden');
    }
    for (let file of reviewForm['images'].files) {
        const imageSrc = URL.createObjectURL(file);
        const imgElement = document.createElement('img');
        imgElement.classList.add('image');
        imgElement.setAttribute('src', imageSrc);
        imageContainerElement.append(imgElement);
    }
});

reviewForm.onsubmit = e => {
    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('placeIndex', reviewForm['placeIndex'].value);
    formData.append('score', reviewForm['score'].value);
    formData.append('content', reviewForm['content'].value);
    for (let file of reviewForm['images'].files) {
        formData.append('images', file);
    }
    xhr.open('POST', './data/review');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'not_signed':
                        alert('로그인되어있지 않습니다. 로그인 후 다시 시도해 주세요.');
                        break;
                    case 'success':
                        // TODO : 리뷰 다시 불러오기
                        break;
                    default:
                        alert('알 수 없는 이유로 리뷰를 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};














