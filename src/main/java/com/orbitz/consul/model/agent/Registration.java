package com.orbitz.consul.model.agent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import org.immutables.value.Value;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;


@Value.Immutable
@JsonSerialize(as = ImmutableRegistration.class)
@JsonDeserialize(as = ImmutableRegistration.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Registration {

    @JsonProperty("Name")
    public abstract String getName();

    @JsonProperty("Id")
    public abstract String getId();

    @JsonProperty("Address")
    public abstract Optional<String> getAddress();

    @JsonProperty("Port")
    public abstract Optional<Integer> getPort();

    @JsonProperty("Check")
    public abstract Optional<RegCheck> getCheck();

    @JsonProperty("Tags")
    public abstract List<String> getTags();


    @Value.Immutable
    @JsonSerialize(as = ImmutableRegCheck.class)
    @JsonDeserialize(as = ImmutableRegCheck.class)
    public abstract static class RegCheck {

        @JsonProperty("Script")
        public abstract Optional<String> getScript();

        @JsonProperty("Interval")
        public abstract Optional<String> getInterval();

        @JsonProperty("TTL")
        public abstract Optional<String> getTtl();

        @JsonProperty("HTTP")
        public abstract Optional<String> getHttp();


        public static RegCheck ttl(long ttl) {
            return ImmutableRegCheck
                    .builder()
                    .ttl(String.format("%ss", ttl))
                    .build();
        }

        public static RegCheck script(String script, long interval) {
            return ImmutableRegCheck
                    .builder()
                    .script(script)
                    .interval(String.format("%ss", interval))
                    .build();
        }

        public static RegCheck http(String http, long interval) {
            return ImmutableRegCheck
                    .builder()
                    .http(http)
                    .interval(String.format("%ss", interval))
                    .build();
        }

        @Value.Check
        protected void validate() {

            checkState(getHttp().isPresent() || getTtl().isPresent() || getScript().isPresent(),
                    "Check must specify either http, ttl, or script");

            if (getHttp().isPresent() || getScript().isPresent()) {
                checkState(getInterval().isPresent(),
                        "Interval must be set if check type is http or script");
            }
        }
        
    }

}
