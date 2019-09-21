import React from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  StatusBar,
  TextInput,
  Text,
  Image,
  View,
} from 'react-native';
import AsyncStorage from '@react-native-community/async-storage';

import config from './../../tools/config';

export default class SignInScreen extends React.Component {
    static navigationOptions = {
      title: '登录',
      headerRight:null,
    };

    constructor(props){
      super(props)

      this.state = {
        name:"",
        passwprd:"",
      }
    }
  
    render() {
      return (
        <View style={styles.container}>
          <StatusBar backgroundColor="white" barStyle="dark-content" />
          <View style={styles.viewContainer}>
            <View style={styles.cellStyle}>
              <Image source={{uri: 'shoujihao'}} style={{width: 16, height: 16}}/>
              <TextInput
                style={styles.inputStyle}
                defaultValue={this.state.name}
                placeholder='请输入手机号'
                returnKeyType='next'
                selectionColor='white'
                underlineColorAndroid='transparent'
                placeholderTextColor='#999999'
                onChangeText={(text) => this.setState({ name: text })}
              />
            </View>
            <View style={styles.cellStyle}>
            <Image source={{uri: 'password'}} style={{width: 16, height: 16}}/>
              <TextInput
                style={styles.inputStyle}
                defaultValue={this.state.passwprd}
                placeholder='请输入登录密码'
                returnKeyType='next'
                selectionColor='white'
                secureTextEntry={true}
                underlineColorAndroid='transparent'
                placeholderTextColor='#999999'
                onChangeText={(text) => this.setState({ passwprd: text })}
              />
            </View>

            <TouchableOpacity style={{marginTop:40, alignSelf:'flex-end'}} activeOpacity={0.8} onPress={this._forgetPwd}>
              <Text style = {{color:'#999999',fontSize:12}}>忘记密码?</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.signStyle} activeOpacity={0.8} onPress={this._signInAsync} >
              <Text style = {{color:'white',fontSize:15}}>登录</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.registerStyle} activeOpacity={0.8} onPress={this._register}>
              <Text style = {{color:'#000',fontSize:15}}>注册</Text>
            </TouchableOpacity>

          </View>
          <Image source={{uri: 'bottom'}} style={{width: config.screenWidth, height: 290*config.screenWidth/993}}/>
        </View>
      );
    }
  
    _signInAsync = async () => {
      await AsyncStorage.setItem('userToken', 'abc');
      this.props.navigation.navigate('App');
    };

    _forgetPwd = () => {
      this.props.navigation.navigate('FindPWD');
    };

    _register = () => {
      this.props.navigation.navigate('Register');
    };

  }

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor:'white',
      alignItems: 'center',
      justifyContent: 'space-between',
    },

    viewContainer: {
      marginTop:40,
      backgroundColor:'white',
      width:config.screenWidth - 40,
      alignItems: 'center',
      justifyContent: 'center',
    },

    cellStyle: {
      backgroundColor:'white',
      flexDirection:'row',
      width:config.screenWidth - 40,
      height:44,
      marginBottom:20,
      alignItems: 'center',
      borderBottomColor:'#efefef',
      borderBottomWidth:config.onePix,
    },

    inputStyle:{
      height:40,
      color:'#000000',
      flex:1,
      alignItems:'center',
      justifyContent:'center',
      marginLeft:4,
      textAlign:'left',
  },

    signStyle: {
      backgroundColor:'#FC427B',
      height:44,
      width:config.screenWidth - 40,
      marginTop:10,
      alignItems: 'center',
      justifyContent: 'center',
    },

    registerStyle: {
      backgroundColor:'white',
      borderColor:'#efefef',
      borderWidth:config.onePix,
      width:config.screenWidth - 40,
      height:44,
      marginTop:10,
      alignItems: 'center',
      justifyContent: 'center',
    },

  });