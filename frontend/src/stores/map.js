export default {
    name: "MapKakao",
    // 전달 받을 값
    props: {
      latitude: {
        type: Number,
        required: true,
      },
      longitude: {
        type: Number,
        required: true,
      },
    },
    data() {
      return {
        map: null
      }
    },
    // 
    mounted() {
      if (window.kakao && window.kakao.maps) {
        this.loadMap();
      }  else {
        this.loadScript();
      }
    },
    methods:{
      loadScript() {
        const script = document.createElement("script");
        script.type = 'text/javascript'
        script.src="//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=ea2f4925f12e1e8253524ecf26e961c0"
        script.onload = () => window.kakao.maps.load(this.loadMap); 
  
        document.head.appendChild(script);
      },
      loadMap() {
        const container = document.getElementById("map"); // 지도를 표시할 div 
        const options = {
          //좌표값 설정
          center: new window.kakao.maps.LatLng(this.latitude, this.longitude), // 지도의 중심좌표
            level: 4, // 지도의 확대 레벨
            mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
        };
  
          this.map = new window.kakao.maps.Map(container, options);

		// 지도의 우측에 확대 축소 컨트롤을 추가한다
		this.map.addControl(new kakao.maps.ZoomControl(), kakao.maps.ControlPosition.RIGHT);

        this.loadMaker();
      },
      loadMaker() {
        const markerPosition = new window.kakao.maps.LatLng(this.latitude, this.longitude);
  
        const marker = new window.kakao.maps.Marker({
          position:markerPosition
        });
  
        marker.setMap(this.map);
      }
    }
  }