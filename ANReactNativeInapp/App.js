/**
 * Sample Ask Nicely React Native App,
 * 
 * Derived from:
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect, useState} from 'react';
import type {Node} from 'react';
import { WebView } from 'react-native-webview';
import uuid from 'react-native-uuid';
import {
  ActivityIndicator,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import {
  Colors
} from 'react-native/Libraries/NewAppScreen';

/* $FlowFixMe[missing-local-annot] The type annotation(s) required by Flow's
 * LTI update could not be added via codemod */
const Section = ({children, title}): Node => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
};

const App: () => Node = () => {
  const [isLoading, setLoading] = useState(true);
  const [showSurvey, setShowSurvey] = useState(false);
  const [surveyUrl, setSurveyUrl] = useState('');
  const [webViewUrl, setWebViewUrl] = useState('');

  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    flex: 1,
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  //surveyslug request
  const getSurveyData = async () => {
    try {
      //We're using the sample-server here, but you'd replace it with your own backend call.
      const response = await fetch('http://localhost:8083');
      const json = await response.json();
      await getSurveySlug(json)
    } catch (error) {
      console.error(error);
    }
  }

  const getSurveySlug = async (surveyData) => {
    const id = uuid.v4().toString().replaceAll("-", "").slice(0, 16);
    const slugUrl = "https://" + surveyData.domain_key + ".asknice.ly/service/inapp.php?" +
      "id=" + id +
      "&anVersion=3.4.1" +
      "&reloadCookie=";

    try {
      const response = await fetch(slugUrl, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...surveyData,
          //Whether to force the survey or not - forcing will result in it ignoring your contact rules
          force: true,
          //Any other variables will be treated as 'custom variables' to be stored against the survey
          custom_variable: 'Something custom to the request'
        })
      });

      const json = await response.json();
      console.log(json)
      if(json.slug) {
        setWebViewUrl(`https://${surveyData.domain_key}.asknice.ly/email/conversation/${json.slug}?template_name=${surveyData.template_name}&inapp=`);
      }
      else {
        console.log("No slug returned; did you disable force?  Server returned: ", json)
      }
    } catch (error) {
      console.error(error);
    }
  }

  const onShowSurvey = () => {
    setShowSurvey(true)
  }

  const onHideSurvey = () => {
    setShowSurvey(false)
  }

  const onMessage = (event) => {
    const { data } = event.nativeEvent

    if (data) {
      jsonData = JSON.parse(data);

      if (jsonData.event === "askNicelyDone" && jsonData.msg === "Success") {
        onHideSurvey()
      }
    }
  }

  useEffect(() => {
    getSurveyData();
    setLoading(false);
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      {!showSurvey ? (
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={backgroundStyle}>
        {isLoading ? (
          <ActivityIndicator />
        ) : (
          <View
            style={{
              backgroundColor: isDarkMode ? Colors.black : Colors.white,
            }}>
            <Section title="Let's Go">
              Click <Text style={styles.highlight} onPress={onShowSurvey}>here</Text> to display your survey
            </Section>
          </View>
        )}
      </ScrollView>
      ) : (
        <WebView
          source={{ uri: webViewUrl }}
          domStorageEnabled
          javaScriptEnabled
          onMessage={onMessage}
        />
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
