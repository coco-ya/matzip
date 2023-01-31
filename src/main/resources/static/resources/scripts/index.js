const mapContainer = document.getElementById('mapContainer');
const searchForm = document.getElementById('searchForm');
const loginButton = document.getElementById('loginButton');
const loginContainer = document.getElementById('loginContainer');
const loginFrame = document.getElementById('loginFrame');

const detailContainer = document.getElementById('detailContainer');
detailContainer.show = (placeObject) => {
    // 지도에 마커 클릭했을 때, 오른쪽 식당 설명 클릭했을 때, 디테일 페이지가 보임
    detailContainer.querySelectorAll('[rel= "title"]').forEach(x => x.innerText = placeObject['name']);

    detailContainer.querySelector('[rel="category"]').innerText = `${placeObject['categoryText']}`;

    // 디테일 폼 안에 리뷰 별점 표시
    detailContainer.querySelector('[rel = "score"]').innerText = placeObject['score'].toFixed(1);

    detailContainer.querySelector('[rel="addressText"]').innerText = `${placeObject['addressPrimary']}${placeObject['addressSecondary'] ? `\n${placeObject['addressSecondary']}` : ''}`;


    detailContainer.querySelector('[rel = "titleReviewCount"]').innerText =
        `${placeObject['reviewCount']}`;
    const openFrom = new Date(placeObject['openFrom']);
    const openTo = new Date(placeObject['openTo']);
    detailContainer.querySelector('[rel="openText"]').innerText = `${placeObject['openFrom']} - ${placeObject['openTo']}`;

    const contact = `${placeObject['contactFirst']}-${placeObject['contactSecond']}-${placeObject['contactThird']}`
    detailContainer.querySelector('[rel = "contactText"]').innerText = contact;
    detailContainer.querySelector('[rel = "contactText"]').setAttribute('href', `tel:${contact}`);



    //리뷰 없으면 등록된 리뷰가 없다고 나오기
    if (placeObject['reviewCount'] === 0) {
        detailContainer.querySelector('[rel="reviewExist"]').classList.add('visible');
    }




    // 홈페이지
    const homepageTextElement = detailContainer.querySelector('[rel="homepageText"]');
    if (placeObject['homepage']) {
        homepageTextElement.innerText = placeObject['homepage'];
        homepageTextElement.setAttribute('href', placeObject['homepage']);
        homepageTextElement.parentElement.parentElement.classList.remove('hidden');
    } else {
        homepageTextElement.parentElement.parentElement.classList.add('hidden');
    }

    detailContainer.querySelector('[rel="descriptionText"]').innerText = placeObject[`description`];
    if (reviewForm) {
        reviewForm['placeIndex'].value = placeObject['index'];
    }

    detailContainer.classList.add('visible');
    loadReviews(placeObject['index']); //리뷰 불러오기 위함
};
detailContainer.hide = () => detailContainer.classList.remove('visible');


detailContainer.querySelector('[rel = "closeButton"]').addEventListener('click', () => {
    detailContainer.hide();
})

const reviewForm = document.getElementById('reviewForm');

const list = document.getElementById('list');

let mapObject; //전역변수

let places = [];

const loadMap = (lat, lng) => {
    mapObject = new kakao.maps.Map(mapContainer, {
        center: new kakao.maps.LatLng(lat, lng), //지도의 중심좌표
        level: 3 //지도의 레벨(확대, 축소 정도)
    });
    kakao.maps.event.addListener(mapObject, 'dragend', () => { // 현재 지도 안에 있는 식당만 나오게하기
        // dragend : 드래그가 끝났을 때 -> 마우스로 지도 끌었을 때마다
        const bounds = mapObject.getBounds();
        const ne = bounds.getNorthEast();
        const sw = bounds.getSouthWest();
        loadPlaces();
    });
    //확대 축소 단계가 변했을 때
    kakao.maps.event.addListener(mapObject, 'zoom_changed', () => {
        loadPlaces();
    })

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
    const formData = new FormData();
    formData.append('minLat', sw['Ma']);
    formData.append('minLng', sw['La']);
    formData.append('maxLat', ne['Ma']);
    formData.append('maxLng', ne['La']);

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

                    //오픈 시간
                    const date = new Date(placeObject['openFrom']);
                    //마감 시간
                    const dateOff = new Date(placeObject['openTo']);
                    //현재시간
                    const currentTime = new Date();

                    const placeHtml = `
                        <li class="item visible" rel= "item">
                            <span class="info">
                            <span class="name-container">
                            <span class="name" rel="name">${placeObject['name']}</span>
                            <span class="category">${placeObject['categoryText']}</span>
                        </span>
                        <span class="rating-container">
                            <span class="star-container">
                                <i class="star fa-solid fa-star ${placeObject['score'] >= 1 ? 'filled' : ''}"></i>
                                <i class="star fa-solid fa-star ${placeObject['score'] >= 2 ? 'filled' : ''}"></i>
                                <i class="star fa-solid fa-star ${placeObject['score'] >= 3 ? 'filled' : ''}"></i>
                                <i class="star fa-solid fa-star ${placeObject['score'] >= 4 ? 'filled' : ''}"></i>
                                <i class="star fa-solid fa-star ${placeObject['score'] >= 5 ? 'filled' : ''}"></i>
                            </span>
                            <span class="score">${placeObject['score'].toFixed(1)}</span>
                           
                            <span class="review-count">리뷰 ${placeObject['reviewCount']}</span>
                            
                        </span>
                        <span class="open-container">
                            <span class="working">
                             ${placeObject['isClose'] === true ? '영업 전' : '영업 중'}
                            </span>
                            <span class="hour"> 
                            ${placeObject['isClose'] === true ? `${placeObject['openFrom']}에 영업 시작`
                        : `${placeObject['breakFrom'] !== null ? `${placeObject['breakFrom']}-${placeObject['breakTo']} 브레이크타임` : `${placeObject['openTo']}에 영업 종료`}`}
                            </span>
                        </span>
                        <span class="address">${placeObject['addressPrimary']}
                        ${placeObject['addressSecondary'] ?? ''}</span>
                        <span class="contact">${placeObject['contactFirst']}-${placeObject['contactSecond']}-${placeObject['contactThird']}</span>
                    </span>
                    <img alt="" class="image" src="./data/placeImage?pi=${placeObject['index']}">
                </li>`;

                    const placeElement = new DOMParser().parseFromString(placeHtml, 'text/html').querySelector('[rel= "item"]');

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

navigator.geolocation.getCurrentPosition(e => { //권한 허용
    loadMap(e['coords']['latitude'], e['coords']['longitude']);
}, () => { // 권한 차단
    loadMap(35.8715411, 128)
})

searchForm['keyword'].addEventListener('input', () => {
    const keyword = searchForm['keyword'].value;
    const itemArray = Array.from(list.querySelectorAll(':scope > [rel = "item"]'));
    for (let item of itemArray) {
        const name = item.querySelector('[rel = "name"]').innerText;
        if (keyword === '' || name.indexOf(keyword) > -1) {
            item.classList.add('visible');
        } else {
            item.classList.remove('visible');
        }
    }
})

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


const reviewContainer = detailContainer.querySelector('[rel = "reviewContainer"]');
const loadReviews = (placeIndex) => {
    //리뷰 불러오기
    reviewContainer.innerHTML = ''; // 리뷰 초기화
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./data/review?pi=${placeIndex}`);
    xhr.onreadystatechange = () => {

        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseArray = JSON.parse(xhr.responseText);
                for (const reviewObject of responseArray) {

                    const itemHtml = `
                    <li class="item" rel="item">
                        <span class="nickname" rel="nickname">${reviewObject['userNickname']}</span>
                        <div class="image-container" rel="imageContainer">
                        </div>
                        <span class="content" rel="content">${reviewObject['content']}</span>
                        <span class="date" rel="date">${reviewObject['date']}</span>

                    </li>`;

                const itemElement = new DOMParser().parseFromString(itemHtml, 'text/html').querySelector('[rel = "item"]');
                const imageContainerElement = itemElement.querySelector('[rel = "imageContainer"]');
                if (reviewObject['imageIndexes'].length > 0) {
                    for (const imageIndex of reviewObject['imageIndexes']) {
                        const imageElement = document.createElement('img');
                        imageElement.setAttribute('alt', '');
                        imageElement.setAttribute('src', `./data/reviewImage?index=${imageIndex}`);
                        imageElement.classList.add('image');
                        imageContainerElement.append(imageElement);
                    }
                } else {
                    imageContainerElement.remove();
                }
                reviewContainer.append(itemElement);
            }
        } else {
            alert('리뷰를 불러오지 못했습니다.');
        }
    }

};
xhr.send();
};


if (reviewForm) { //리뷰 작성 폼은 로그인 안하면 안보임 - 리뷰 폼이 있는가?
    reviewForm.querySelector('[rel="submitButton"]').addEventListener('click', e => {
        e.preventDefault();
        if (reviewForm['score'].value === '0') {
            alert('별점을 선택해주세요.');
            return false;
        }

        if (reviewForm['content'].value === '') {
            alert('내용을 입력해주세요.');
            reviewForm['content'].focus();
            return false;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('score', reviewForm['score'].value);
        formData.append('content', reviewForm['content'].value);
        formData.append('placeIndex', reviewForm['placeIndex'].value); //맛집 인덱스

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
                            alert('로그인이 되어있지않습니다.');
                            break;
                        case 'success':
                            loadReviews(reviewForm['placeIndex'].value); // 성공일 때 리뷰를 불러옴
                            alert('리뷰를 등록하였습니다.');
                            break;
                        default:
                            alert('알 수 없는 오류');
                    }
                } else {
                    alert('서버와 통신 못하지 못하였습니다.');
                }
            }
        }
        xhr.send(formData);
    });

    reviewForm.querySelector('[rel="imageSelectButton"]').addEventListener('click', e => {
        e.preventDefault();
        reviewForm['images'].click();
    });

//리뷰 별 색 채우기
    const reviewStarArray = Array.from(reviewForm.querySelector('[rel = "starContainer"]').querySelectorAll(':scope > .star'));

    for (let i = 0; i < reviewStarArray.length; i++) {
        reviewStarArray[i].addEventListener('click', () => {
            reviewStarArray.forEach(x => x.classList.remove('selected'));
            //일단 selected 다 제거해주고
            for (let j = 0; j <= i; j++) {
                reviewStarArray[j].classList.add('selected');
                // 0부터 클릭 된 별까지 selected 줘서 채워줌
            }
            //선택된 별 갯수+1 => 리뷰 점수
            reviewForm.querySelector('[rel = "score"]').innerText = i + 1;
            reviewForm['score'].value = i + 1;
        });
    }


    reviewForm['images'].addEventListener('input', () => {
        const imageContainerElement = reviewForm.querySelector('[rel="imageContainer"]');
        imageContainerElement.querySelectorAll('img.image').forEach(x => x.remove());

        if (reviewForm['images'].files.length > 0) { //선택한 파일이 있다
            reviewForm.querySelector('[rel = "noImage"]').classList.add('hidden');
        } else {//선택한 파일이 없다
            reviewForm.querySelector('[rel = "noImage"]').classList.remove('hidden');
        }

        //파일 올리기
        for (let file of reviewForm['images'].files) {
            const imageSrc = URL.createObjectURL(file);
            const imgElement = document.createElement('img');
            imgElement.classList.add('image');
            imgElement.setAttribute('src', imageSrc);
            imageContainerElement.append(imgElement);
        }
    });


}



