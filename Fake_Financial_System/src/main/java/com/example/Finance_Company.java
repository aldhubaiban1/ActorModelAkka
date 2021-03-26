package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class Finance_Company extends AbstractBehavior<Finance_Company.Command> {

    public interface Command{}

//public static final class ReadValue implements Command {
//    final String companyId;
//    public ReadValue(String companyId) {
//        this.companyId = companyId;
//    }
//}
//
//    public static final class RespondValue {
//        final String companyId;
//        final double value;
//
//        public RespondValue(String companyId, double value) {
//            this.companyId = companyId;
//            this.value = value;
//        }
//    }
//
//    public static final class SaveValue implements Command {
//        final String companyId;
//        final double value;
//
//        public SaveValue(String companyId, double value) {
//            this.companyId = companyId;
//            this.value = value;
//        }
//    }
//
//    public static final class ValueSaved {
//        final String companyId;
//
//        public ValueSaved(String companyId) {
//            this.companyId = companyId;
//        }
//    }



    static Behavior<Finance_Company.Command> create(String companyId , double value) {
        return Behaviors.setup(context -> new Finance_Company(context,companyId,value));
    }

    private final String companyId;
    private double lastCompanyValue = -1;

    private Finance_Company(ActorContext<Command> context, String companyId,double value) {
        super(context);
        this.companyId = companyId;
        lastCompanyValue= value;
        getContext().getLog().info("Company created {}. Value of company: {}",companyId,lastCompanyValue);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
//                .onMessage(ReadValue.class,this::onReadValue)
//                .onMessage(SaveValue.class,this::onSaveValue)

                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

//    private Behavior<Command> onSaveValue(M ) {
//        getContext().getLog().info("Saved value {} on {}", s.value, s.companyId);
//
//        lastCompanyValue=s.value;
//        return this;
//    }
//
//    private Behavior<Command> onReadValue(ReadValue r) {
//        r.replyToRespondValue.tell(new RespondValue(r.companyId,lastCompanyValue));
//        return this;
//    }


    private Behavior<Command> onPostStop() {
        getContext().getLog().info("Company actor stopped");
        return Behaviors.stopped();
    }
}















