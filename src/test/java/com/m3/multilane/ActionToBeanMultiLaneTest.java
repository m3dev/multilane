package com.m3.multilane;

import com.m3.multilane.action.SimpleAction;
import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.Left;
import com.m3.scalaflavor4j.Right;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ActionToBeanMultiLaneTest {

    TestServer server = new TestServer(8903);

    @Before
    public void setUp() throws Exception {
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }


    @Test
    public void type() throws Exception {
        assertThat(ActionToBeanMultiLane.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        ActionToBeanMultiLane target = new ActionToBeanMultiLane();
        assertThat(target, notNullValue());
    }

    public static class Profile {

        private String name;

        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    @Test
    public void collectValuesAsBean_A$Class() throws Exception {

        ActionToBeanMultiLane multiLane = new ActionToBeanMultiLane();

        multiLane.start("name", new SimpleAction<String, String>("alice", 1000) {
            public Either<Throwable, String> apply() {
                return Right._(getInput().toUpperCase());
            }
        });
        multiLane.start("age", new SimpleAction<Integer, Integer>(10, 1000) {
            public Either<Throwable, Integer> apply() {
                return Right._(getInput() * 2);
            }
        });

        Class<Profile> clazz = Profile.class;
        Profile profile = multiLane.collectValuesAsBean(clazz);
        assertThat(profile.getName(), is(equalTo("ALICE")));
        assertThat(profile.getAge(), is(equalTo(20)));
    }

    @Test
    public void collectValuesAsBean_A$Class_timeout() throws Exception {

        ActionToBeanMultiLane multiLane = new ActionToBeanMultiLane();

        multiLane.start("name", new SimpleAction<String, String>("alice", 1) {
            public Either<Throwable, String> apply() {
                try {
                    Thread.sleep(10L);
                } catch (Throwable e) {
                    return Left._(e);
                }
                return Right._(getInput().toUpperCase());
            }
        });
        multiLane.start("age", new SimpleAction<Integer, Integer>(10, 1000) {
            public Either<Throwable, Integer> apply() {
                return Right._(getInput() * 2);
            }
        });

        Class<Profile> clazz = Profile.class;
        Profile profile = multiLane.collectValuesAsBean(clazz);
        assertThat(profile.getName(), is(nullValue()));
        assertThat(profile.getAge(), is(equalTo(20)));
    }

    @Test
    public void collectValuesAsBean_A$Class_typeError() throws Exception {
        ActionToBeanMultiLane multiLane = new ActionToBeanMultiLane();
        multiLane.start("name", new SimpleAction<Integer, Integer>(10, 1000) {
            public Either<Throwable, Integer> apply() {
                return Right._(getInput() * 2);
            }
        });
        multiLane.start("age", new SimpleAction<String, String>("alice", 1000) {
            public Either<Throwable, String> apply() {
                return Right._(getInput().toUpperCase());
            }
        });
        Class<Profile> clazz = Profile.class;
        Profile profile = multiLane.collectValuesAsBean(clazz);
        assertThat(profile.getName(), is("20"));
        assertThat(profile.getAge(), is(0));
    }

    @Test
    public void collectValuesAsBean_A$Class_invalidName() throws Exception {
        ActionToBeanMultiLane multiLane = new ActionToBeanMultiLane();
        multiLane.start("ageg", new SimpleAction<Integer, Integer>(10, 1000) {
            public Either<Throwable, Integer> apply() {
                return Right._(getInput() * 2);
            }
        });
        Class<Profile> clazz = Profile.class;
        Profile profile = multiLane.collectValuesAsBean(clazz);
        assertThat(profile.getName(), is(nullValue()));
        assertThat(profile.getAge(), is(nullValue()));
    }

}
