package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.text.DecimalFormat;


public class Finance_Company extends AbstractBehavior<Finance_Company.Command> {

    public interface Command{}



    public enum RespondValue implements Command {
        INSTANCE
    }

    public static final class ChangeValue implements Command {
        final String companyId;
        final double value;

        public ChangeValue(String companyId, double value) {
            this.companyId = companyId;
            this.value = value;
        }
    }





    static Behavior<Finance_Company.Command> create(String companyId , double value,ActorRef<Finance_QuoteGenerator.Command> replyToQuoteGenerator) {
        return Behaviors.setup(context -> new Finance_Company(context,companyId,value,replyToQuoteGenerator));
    }

    private final String companyId;
    private double lastCompanyValue = -1;
    private ActorRef<Finance_QuoteGenerator.Command> replyToQuoteGenerator;

    private Finance_Company(ActorContext<Command> context, String companyId,double value, ActorRef<Finance_QuoteGenerator.Command> replyToQuoteGenerator) {
        super(context);
        this.companyId = companyId;
        lastCompanyValue= value;
        this.replyToQuoteGenerator=replyToQuoteGenerator;
        getContext().getLog().info("Company created {}. Value of company: {}",companyId,lastCompanyValue);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals(RespondValue.INSTANCE,this::onRespondValue)
                .onMessage(ChangeValue.class,this::onChangeValue)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }



    private Behavior<Command> onChangeValue(ChangeValue changevalue ) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        if (changevalue.value>3) {
            lastCompanyValue += ((changevalue.value / 100) * lastCompanyValue);
            lastCompanyValue=Double.valueOf(df2.format(lastCompanyValue));
            getContext().getLog().info("Company value increased by {}%  {}", changevalue.value,lastCompanyValue, changevalue.companyId);
        }
        else{
            lastCompanyValue -= ((changevalue.value / 100) * lastCompanyValue);
            lastCompanyValue=Double.valueOf(df2.format(lastCompanyValue));
            getContext().getLog().info("Company value decreased by {}%  {}", changevalue.value,lastCompanyValue, changevalue.companyId);
        }
        replyToQuoteGenerator.tell(new Finance_QuoteGenerator.GetLatestValues(changevalue.companyId, lastCompanyValue));
        return this;
    }


    private Behavior<Command> onRespondValue() {
        getContext().getLog().info("value:{}",lastCompanyValue);
        return this;
    }


    private Behavior<Command> onPostStop() {
        getContext().getLog().info("Company actor stopped");
        return Behaviors.stopped();
    }
}















