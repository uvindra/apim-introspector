# apim-introspector
An attempt at logging API Manager invocations based on the presence of a custom header being sent with the API invocation

#### Sample API with required mediators and handler engaged

```
<?xml version="1.0" encoding="UTF-8"?><api xmlns="http://ws.apache.org/ns/synapse" name="admin--weather" context="/weather" version="1.0.0" version-type="url">
    <resource methods="POST GET OPTIONS DELETE PUT" url-mapping="/*" faultSequence="none">
        <inSequence>
            <property name="POST_TO_URI" value="true" scope="axis2"/>
            <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                <then>
		    <class name="org.wso2.sample.synapse.introspection.InsequenceIntrospector"></class>
		    <send>
                        <endpoint name="admin--weather_APIproductionEndpoint_0">
                            <http uri-template="http://api.openweathermap.org/data/2.5/weather">
                                <timeout>
                                    <duration>30000</duration>
                                    <responseAction>fault</responseAction>
                                </timeout>
                                <suspendOnFailure>
                                    <errorCodes>-1</errorCodes>
                                    <initialDuration>0</initialDuration>
                                    <progressionFactor>1.0</progressionFactor>
                                    <maximumDuration>0</maximumDuration>
                                </suspendOnFailure>
                                <markForSuspension>
                                    <errorCodes>-1</errorCodes>
                                </markForSuspension>
                            </http>
                        </endpoint>
                    </send>		    
                </then>
                <else>
                    <sequence key="_sandbox_key_error_"/>
                </else>
            </filter>
        </inSequence>
        <outSequence>
	    <class name="org.wso2.sample.synapse.introspection.OutsequenceIntrospector"></class>
            <send/>
        </outSequence>
    </resource>
    <handlers>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler"/>
        <handler class="org.wso2.sample.synapse.introspection.APIIntrospectHandler"/>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.throttling.APIThrottleHandler">
            <property name="id" value="A"/>
            <property name="policyKey" value="gov:/apimgt/applicationdata/tiers.xml"/>
        </handler>
        <handler class="org.wso2.carbon.apimgt.usage.publisher.APIMgtUsageHandler"/>
        <handler class="org.wso2.carbon.apimgt.usage.publisher.APIMgtGoogleAnalyticsTrackingHandler">
            <property name="configKey" value="gov:/apimgt/statistics/ga-config.xml"/>
        </handler>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerExtensionHandler"/>
    </handlers>
</api>
```

#### Sample invocation with custom header to engage introspector
```
curl -v -H 'Authorization: Bearer xxxxxxxxxxxxxxxxxxxxxxxxx' -H 'X-Introspect: true' http://localhost:8280/weather/1.0.0?q=colombo
