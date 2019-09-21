import React,{Component} from 'react';
import {View, Text, Image} from 'react-native';
import PropTypes from 'prop-types';

export default class EmptyView extends Component{

    render(){
        return (
            <View style={{flex:1,justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
                <Image source={{uri: 'doctor'}} style={{width: 160, height: 150}}/>
                <Text style={{fontSize:14,marginTop:4,color:'#999999'}}>{this.props.emptyTitle}</Text>
            </View>
        )
    }
}

EmptyView.propTypes = {
    emptyTitle:PropTypes.string,
};

EmptyView.defaultProps = {
    emptyTitle:'这里没有内容',
};