import {PixelRatio,Dimensions} from 'react-native';

export default {
    screenWidth:Dimensions.get('window').width,
    screenHeight:Dimensions.get('window').height,
    themeColor: '#167DF7',
    onePix:1/PixelRatio.get(),
};