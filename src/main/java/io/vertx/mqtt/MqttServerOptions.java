/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.mqtt;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.impl.Arguments;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.core.net.TrustOptions;
import java.util.concurrent.TimeUnit;

/**
 * Represents options used by the MQTT server
 */
@DataObject(generateConverter = true, publicConverter = false)
public class MqttServerOptions extends NetServerOptions {

  public static final int DEFAULT_PORT = 1883; // Default port is 1883 for MQTT
  public static final int DEFAULT_TLS_PORT = 8883; // Default TLS port is 8883 for MQTT

  public static final int DEFAULT_MAX_MESSAGE_SIZE = 8092;
  public static final boolean DEFAULT_AUTO_CLIENT_ID = true;
  public static final int DEFAULT_MAX_CLIENT_ID_LENGTH = 23;
  public static final int DEFAULT_TIMEOUT_ON_CONNECT = 90;
  public static final boolean DEFAULT_USE_WEB_SOCKET = false;
  public static final int DEFAULT_WEB_SOCKET_MAX_FRAME_SIZE = 65536;
  public static final boolean DEFAULT_PER_FRAME_WEBSOCKET_COMPRESSION_SUPPORTED = true;
  public static final boolean DEFAULT_PER_MESSAGE_WEBSOCKET_COMPRESSION_SUPPORTED = true;
  public static final int DEFAULT_WEBSOCKET_COMPRESSION_LEVEL = 6;
  public static final boolean DEFAULT_WEBSOCKET_ALLOW_SERVER_NO_CONTEXT = false;
  public static final boolean DEFAULT_WEBSOCKET_PREFERRED_CLIENT_NO_CONTEXT = false;


  public static final String MQTT_SUBPROTOCOL_CSV_LIST = "mqtt, mqttv3.1, mqttv3.1.1";

  // max message size (variable header + payload) in bytes
  private int maxMessageSize;
  // if clientid should be autogenerated (if "zero-bytes")
  private boolean isAutoClientId;
  // max client id length
  private int maxClientIdLength;
  // timeout on CONNECT packet
  private int timeoutOnConnect;
  // if websocket should be used
  private boolean useWebSocket;
  // max WebSocket frame size
  private int webSocketMaxFrameSize;
  // per frame WebSocket compression supported
  private boolean perFrameWebSocketCompressionSupported;
  // per message WebSocket compression supported
  private boolean perMessageWebSocketCompressionSupported;
  // WebSocket compression level
  private int webSocketCompressionLevel;
  // WebSocket allow server no context
  private boolean webSocketAllowServerNoContext;
  // WebSocket preferred client no context
  private boolean webSocketPreferredClientNoContext;

  /**
   * Default constructor
   */
  public MqttServerOptions() {
    super();
    init();
  }

  private void init() {
    // override the default port
    this.setPort(DEFAULT_PORT);
    this.maxMessageSize = DEFAULT_MAX_MESSAGE_SIZE;
    this.isAutoClientId = DEFAULT_AUTO_CLIENT_ID;
    this.maxClientIdLength = DEFAULT_MAX_CLIENT_ID_LENGTH;
    this.timeoutOnConnect = DEFAULT_TIMEOUT_ON_CONNECT;
    this.useWebSocket = DEFAULT_USE_WEB_SOCKET;
    this.webSocketMaxFrameSize = DEFAULT_WEB_SOCKET_MAX_FRAME_SIZE;
    this.perFrameWebSocketCompressionSupported = DEFAULT_PER_FRAME_WEBSOCKET_COMPRESSION_SUPPORTED;
    this.perMessageWebSocketCompressionSupported = DEFAULT_PER_MESSAGE_WEBSOCKET_COMPRESSION_SUPPORTED;
    this.webSocketCompressionLevel = DEFAULT_WEBSOCKET_COMPRESSION_LEVEL;
    this.webSocketAllowServerNoContext = DEFAULT_WEBSOCKET_ALLOW_SERVER_NO_CONTEXT;
    this.webSocketPreferredClientNoContext = DEFAULT_WEBSOCKET_PREFERRED_CLIENT_NO_CONTEXT;
  }

  /**
   * Create an options from JSON
   *
   * @param json the JSON
   */
  public MqttServerOptions(JsonObject json) {
    super(json);
    init();

    MqttServerOptionsConverter.fromJson(json, this);

    if ((this.maxMessageSize > 0) && (this.getReceiveBufferSize() > 0)) {
      Arguments.require(this.getReceiveBufferSize() >= this.maxMessageSize,
        "Receiver buffer size can't be lower than max message size");
    }
  }

  /**
   * Copy constructor
   *
   * @param other the options to copy
   */
  public MqttServerOptions(MqttServerOptions other) {
    super(other);

    this.maxMessageSize = other.maxMessageSize;
    this.isAutoClientId = other.isAutoClientId;
    this.maxClientIdLength = other.maxClientIdLength;
    this.timeoutOnConnect = other.timeoutOnConnect;
    this.useWebSocket = other.useWebSocket;
    this.webSocketMaxFrameSize = other.webSocketMaxFrameSize;
    this.perFrameWebSocketCompressionSupported = other.perFrameWebSocketCompressionSupported;
    this.perMessageWebSocketCompressionSupported = other.perMessageWebSocketCompressionSupported;
    this.webSocketCompressionLevel = other.webSocketCompressionLevel;
    this.webSocketAllowServerNoContext = other.webSocketAllowServerNoContext;
    this.webSocketPreferredClientNoContext = other.webSocketAllowServerNoContext;
  }

  @Override
  public MqttServerOptions setPort(int port) {
    super.setPort(port);
    return this;
  }

  @Override
  public MqttServerOptions setHost(String host) {
    super.setHost(host);
    return this;
  }

  @Override
  public MqttServerOptions setClientAuth(ClientAuth clientAuth) {
    super.setClientAuth(clientAuth);
    return this;
  }

  @Override
  public MqttServerOptions setSsl(boolean ssl) {
    super.setSsl(ssl);
    return this;
  }

  @Override
  public MqttServerOptions setKeyCertOptions(KeyCertOptions options) {
    super.setKeyCertOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions setTrustOptions(TrustOptions options) {
    super.setTrustOptions(options);
    return this;
  }

  @Override
  public MqttServerOptions addEnabledCipherSuite(String suite) {
    super.addEnabledCipherSuite(suite);
    return this;
  }

  @Override
  public MqttServerOptions addEnabledSecureTransportProtocol(final String protocol) {
    super.addEnabledSecureTransportProtocol(protocol);
    return this;
  }

  @Override
  public MqttServerOptions addCrlPath(String crlPath) throws NullPointerException {
    super.addCrlPath(crlPath);
    return this;
  }

  @Override
  public MqttServerOptions addCrlValue(Buffer crlValue) throws NullPointerException {
    super.addCrlValue(crlValue);
    return this;
  }

  @Override
  public MqttServerOptions setReceiveBufferSize(int receiveBufferSize) {
    if ((this.maxMessageSize > 0) && (receiveBufferSize > 0)) {
      Arguments.require(receiveBufferSize >= this.maxMessageSize,
        "Receiver buffer size can't be lower than max message size");
    }
    super.setReceiveBufferSize(receiveBufferSize);
    return this;
  }

  @Override
  public MqttServerOptions setSni(boolean sni) {
    super.setSni(sni);
    return this;
  }

  /**
   * Set max MQTT message size
   *
   * @param maxMessageSize  max MQTT message size (variable header + payload)
   * @return  MQTT server options instance
   */
  public MqttServerOptions setMaxMessageSize(int maxMessageSize) {
    Arguments.require(maxMessageSize > 0, "maxMessageSize must be > 0");
    if (this.getReceiveBufferSize() > 0) {
      Arguments.require(this.getReceiveBufferSize() >= maxMessageSize,
        "Receiver buffer size can't be lower than max message size");
    }
    this.maxMessageSize = maxMessageSize;
    return this;
  }

  /**
   * @return  max MQTT message size (variable header + payload)
   */
  public int getMaxMessageSize() {
    return this.maxMessageSize;
  }

  /**
   * Set if clientid should be auto-generated when it's "zero-bytes"
   *
   * @param isAutoClientId
   * @return  MQTT server options instance
   */
  public MqttServerOptions setAutoClientId(boolean isAutoClientId) {
    this.isAutoClientId = isAutoClientId;
    return this;
  }

  /**
   * @return  if clientid should be auto-generated when it's "zero-bytes" (default is true)
   */
  public boolean isAutoClientId() {
    return this.isAutoClientId;
  }

  /**
   * @return the max client id length
   */
  public int getMaxClientIdLength() {
    return maxClientIdLength;
  }

  /**
   * Set the max client id length.
   *
   * @param maxClientIdLength the new value
   * @return  MQTT server options instance
   */
  public MqttServerOptions setMaxClientIdLength(int maxClientIdLength) {
    Arguments.require(maxClientIdLength > 0, "maxClientIdLength must be > 0");
    this.maxClientIdLength = maxClientIdLength;
    return this;
  }

  /**
   * Set the timeout on CONNECT packet
   *
   * @param timeoutOnConnect timeout on CONNECT before closing connection
   * @return  MQTT server options instance
   */
  public MqttServerOptions setTimeoutOnConnect(int timeoutOnConnect) {
    this.timeoutOnConnect = timeoutOnConnect;
    return this;
  }

  @Override
  public MqttServerOptions setUseProxyProtocol(boolean useProxyProtocol) {
    super.setUseProxyProtocol(useProxyProtocol);
    return this;
  }

  @Override
  public boolean isUseProxyProtocol() {
    return super.isUseProxyProtocol();
  }

  @Override
  public long getProxyProtocolTimeout() {
    return super.getProxyProtocolTimeout();
  }

  @Override
  public MqttServerOptions setProxyProtocolTimeout(long proxyProtocolTimeout) {
    super.setProxyProtocolTimeout(proxyProtocolTimeout);
    return this;
  }

  @Override
  public MqttServerOptions setProxyProtocolTimeoutUnit(
    TimeUnit proxyProtocolTimeoutUnit) {
    super.setProxyProtocolTimeoutUnit(proxyProtocolTimeoutUnit);
    return this;
  }

  @Override
  public TimeUnit getProxyProtocolTimeoutUnit() {
    return super.getProxyProtocolTimeoutUnit();
  }

  /**
   * @return  timeout on CONNECT before closing connection
   */
  public int timeoutOnConnect() {
    return this.timeoutOnConnect;
  }

  /**
   * enable mqtt over websocket
   *
   * @param useWebSocket use mqtt over websocket
   * @return  MQTT server options instance
   */
  public MqttServerOptions setUseWebSocket(boolean useWebSocket) {
    this.useWebSocket = useWebSocket;
    return this;
  }

  /**
   * @return  use mqtt over websocket
   */
  public boolean isUseWebSocket() {
    return useWebSocket;
  }

  /**
   * @return the WebSocket max frame size
   */
  public int getWebSocketMaxFrameSize() {
    return webSocketMaxFrameSize;
  }

  /**
   * Set the WebSocket max frame size.
   *
   * <p> This should be used when WebSocket transport is used and {@link #maxMessageSize} is larger than the WebSocket frame size
   *
   * @param webSocketMaxFrameSize the new frame size
   */
  public void setWebSocketMaxFrameSize(int webSocketMaxFrameSize) {
    Arguments.require(webSocketMaxFrameSize > 0, "WebSocket max frame size must be > 0");
    this.webSocketMaxFrameSize = webSocketMaxFrameSize;
  }

  /**
   * Get whether WebSocket the per-frame deflate compression extension is supported.
   *
   * @return {@code true} if the http server will accept the per-frame deflate compression extension
   */
  public boolean isPerFrameWebSocketCompressionSupported() {
    return perFrameWebSocketCompressionSupported;
  }

  /**
   * Enable or disable support for the WebSocket per-frame deflate compression extension.
   *
   * @param supported {@code true} when the per-frame deflate compression extension is supported
   * @return a reference to this, so the API can be used fluently
   */
  public MqttServerOptions setPerFrameWebSocketCompressionSupported(boolean supported) {
    this.perFrameWebSocketCompressionSupported = supported;
    return this;
  }


  /**
   * Get whether WebSocket the per-frame deflate compression extension is supported.
   *
   * @return {@code true} if the http server will accept the per-frame deflate compression extension
   */
  public boolean isPerMessageWebSocketCompressionSupported() {
    return perMessageWebSocketCompressionSupported;
  }

  /**
   * Enable or disable support for WebSocket per-message deflate compression extension.
   *
   * @param supported {@code true} when the per-message WebSocket compression extension is supported
   * @return a reference to this, so the API can be used fluently
   */
  public MqttServerOptions setPerMessageWebSocketCompressionSupported(boolean supported) {
    this.perMessageWebSocketCompressionSupported = supported;
    return this;
  }

  /**
   * @return the current WebSocket deflate compression level
   */
  public int getWebSocketCompressionLevel() {
    return webSocketCompressionLevel;
  }

  /**
   * Set the WebSocket compression level.
   *
   * @param compressionLevel the compression level
   * @return a reference to this, so the API can be used fluently
   */
  public MqttServerOptions setWebSocketCompressionLevel(int compressionLevel) {
    this.webSocketCompressionLevel = compressionLevel;
    return this;
  }

  /**
   * @return {@code true} when the WebSocket server will accept the {@code server_no_context_takeover} parameter for the per-message
   * deflate compression extension offered by the client
   */
  public boolean isWebSocketAllowServerNoContext() {
    return webSocketAllowServerNoContext;
  }

  /**
   * Set whether the WebSocket server will accept the {@code server_no_context_takeover} parameter of the per-message
   * deflate compression extension offered by the client.
   *
   * @param accept {@code true} to accept the {@literal server_no_context_takeover} parameter when the client offers it
   * @return a reference to this, so the API can be used fluently
   */
  public MqttServerOptions setWebSocketAllowServerNoContext(boolean accept) {
    this.webSocketAllowServerNoContext = accept;
    return this;
  }

  /**
   * @return {@code true} when the WebSocket server will accept the {@code client_no_context_takeover} parameter for the per-message
   * deflate compression extension offered by the client
   */
  public boolean isWebSocketPreferredClientNoContext() {
    return webSocketPreferredClientNoContext;
  }

  /**
   * Set whether the WebSocket server will accept the {@code client_no_context_takeover} parameter of the per-message
   * deflate compression extension offered by the client.
   *
   * @param accept {@code true} to accept the {@code client_no_context_takeover} parameter when the client offers it
   * @return a reference to this, so the API can be used fluently
   */
  public MqttServerOptions setWebSocketPreferredClientNoContext(boolean accept) {
    this.webSocketPreferredClientNoContext = accept;
    return this;
  }

  @Override
  public JsonObject toJson() {
    JsonObject json = super.toJson();
    MqttServerOptionsConverter.toJson(this, json);
    return json;
  }
}
