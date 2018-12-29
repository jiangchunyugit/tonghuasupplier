<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>设计家平台入驻协议</title>
    <style>
        body {
            font-family: SimSun;
        }

        .text-algin {
            text-align: center;
        }

        .inline-block {
            display: inline-block;
        }
        .textput {
            margin: 0;
            padding: 0 10px;
            min-height: 20px;
            line-height: 20px;
            border:none;
            border-bottom: 1px solid #000;
            outline:none;
            font-size: 16px;
            display: inline-block;
        }
        div.textput{
            margin-bottom: -7px;
            margin-right: 3px;
        }
        .defaultwidth{
            width: 380px;
        }
        .mintextput{
            width: 200px !important;
            text-align: center;
            text-indent: 0;
        }
        .smintextput{
            width: 130px !important;
            text-align: center;
            text-indent: 0;
        }
        .smalltextput{
            min-width: 60px !important;
            text-align: center;
            text-indent: 0;
        }
        .placeholder {
            margin: 0;
            display: inline-block;
            width: 200px;
            height: 30px;
            line-height: 30px;
            border-bottom: 1px solid #000;
            text-align: center;
            text-indent: 0;

        }

        .placeholderT {
            margin: 0;
            display: inline-block;
            width: 100px;
            height: 20px;
            line-height: 20px;
            border-bottom: 1px solid #000;
            text-align: center;
            text-indent: 0;
        }

        .placeholderK {
            margin: 0;
            display: inline-block;
            width: 50px;
            height: 20px;
            line-height: 20px;
            border-bottom: 1px solid #000;
            text-align: center;
            text-indent: 0;
        }

        .indent{
            text-indent: 2em;
        }
        div.indent{
            text-indent: 4em;
        }

        .list {
            margin-left: 1%;
        }

        section {
            line-height: 30px;
        }
    </style>
</head>

<body>
<h1 class="text-algin">“居然设计家”平台入驻合作合同（2019版）</h1>
<div>
    <h4 class="inline-block">甲方: </h4>
    <input name="firstParty" class="textput defaultwidth" type="text" value="${c01}">
</div>
<div>
    <h4 class="inline-block">乙方：</h4>
    <input name="secondParty" class="textput defaultwidth" type="text" value="${c02}">
</div>
<section class="indent">
    “居然设计家”平台（以下简称平台）是由居然之家打造的融线上线下为一体的智能设计和家装管理平台，包括设计云平台、家具材料采购平台、商品销售平台、施工管理平台、物流配送平台和智能家居服务平台六大部分。平台宗旨是以互联网、大数据、云计算以及人工智能的技术应用为手段，改善家居消费者的服务体验、提升家居行业运行效率，从而让家居服务变得快乐简单，并以此为基础构建以家居为中心的家庭消费生态圈。
</section>
<section class="indent">
    通过平台，设计师可以使用在线设计工具，节省施工图、预算的制作成本，还可以享受平台的厂商资源，大大提升工作效率和经济效益。
</section>
<section class="indent">
    基于此，甲乙双方同意建立业务合作伙伴关系。经甲乙双方协商一致，根据《中华人民共和国合同法》及有关法律法规，签订本协议。
</section>
<h4>一、合作约定</h4>
<section class="indent">
    甲方向乙方（<input type="checkbox" name="designStudio">设计工作室<input type="checkbox">设计师） 提供线上设计云平台、材料采购平台和施工管理平台资源。 并对乙方进行宣传和推广。乙方可使用3D云设计（Homestyler）软件进行方案制作， 向甲方客户提供家居全案设计、产品推荐购买服务和施工服务。甲方按本合同约定向乙方返设计费、收取平台服务管理费，并向乙方支付材料推荐服务费和施工服务费。
</section>
<section class="indent">
    合作期限自<div class="textput smalltextput">${startTime?string('yyyy')}</div>年<div class="textput smalltextput">${startTime?string('MM')}</div>月<div class="textput smalltextput">${startTime?string('dd')}</div>日
    至<div class="textput smalltextput">${endTime?string('yyyy')}</div>年<div class="textput smalltextput">${endTime?string('MM')}</div>月<div class="textput smalltextput">${endTime?string('dd')}</div>日。
    乙方在经营过程中，应认同并遵守《居然设计家设计行业公约》（附件1）；乙方应本着诚信原则为甲方客户提供服务。
</section>
<section class="indent">
    乙方指定返设计费、材料推荐服务费、施工服务费等其他相关款项的账户信息为：
    <#if  c22 == 1>
    <div class="indent">
        户名： <input name="secondParty" class="textput defaultwidth" type="text" value="${c05}">
    </div>
    <div class="indent">
        开户行： <input name="secondParty" class="textput defaultwidth" type="text" value="${c06}">
    </div>
    <div class="indent">
        账号：  <input name="secondParty" class="textput defaultwidth" type="text" value="${c07}">
    </div>
    <#else>
    <div class="indent">
        开户行： <input name="secondParty" class="textput defaultwidth" type="text" value="居然金融">
    </div>
    <div class="indent">
        账号：  <input name="secondParty" class="textput defaultwidth" type="text" value="${c07}">
    </div>
    </#if>
</section>
<h4>二、甲方的权利与义务</h4>
<section class='indent'>
    1．甲方负责平台的管理及运营，关于平台使用对乙方进行培训，配备专业人员对接设计、材料、施工等业务并辅助乙方解决在平台使用过程中遇到的各类问题。
</section>
<section class="indent">
    2．甲方为乙方提供强大的技术支持，维护安全、有序的线上经营环境，保障乙方合法权益。甲方为乙方开通平台管理员账号，可使用客户订单数据查询、业务流量查询、作品案例管理等功能。
</section>
<section class="indent">
    3．甲方根据乙方的设计风格、设计费用等项对设计项目订单进行统一派单。在乙方接单过程中，若出现交付的设计方案差、接单反响慢、售后不到位，或出现客诉、挑单、拒单等情况，甲方有权对乙方进行停单处理，并有权要求乙方重新安排项目负责人，所产生的一切费用由乙方承担。
</section>
<section class="indent">
    4．甲方为乙方提供材料采购平台，提供丰富的材料品牌库，以满足乙方设计使用，并按时与乙方进行返款结算，保障乙方材料推荐的权益。
</section>
<section class="indent">
    5.甲方向乙方提供施工落地服务，在施工过程中提供施工管理工具，便于工地管理及服务。
</section>
<section class="indent">
    6.甲方为乙方提供宣传及学习机会，包含但不限于甲方主办或参与的比赛、设计家官网或自媒体、设计论坛、品牌活动和游学等，帮助乙方提高知名度，获取更多客户资源。
</section>
<section class="indent">
    7.甲方有权在乙方违反法律法规、违反设计行业准则、违反甲方规定及出现重大投诉事故等情况下解除与乙方的合作关系。
</section>

<h4>三、乙方的权利与义务</h4>
<section class="indent">
    1.乙方承接甲方平台业务需进行实名认证。设计费标准根据平台运营规则进行设定，并由甲方在平台上进行公示。乙方承接甲方客户委托的设计项目，须使用甲方制式《“居然设计家”室内装饰设计合同》（以下简称《设计合同》）；推荐材料购买，需使用甲方制式《居然之家商品销售合同》（以下简称《销售合同》）；承接工程项目，须使用甲方制式《家庭居室装饰装修施工合同》（以下简称《施工合同》）。甲方平台统一代收各项费用。
</section>
<section class="indent">
    2.乙方必须使用居然设计家提供的设计云平台、材料采购平台、施工管理平台进行相关业务活动，并按照甲方制定的流程为客户提供设计、选材、施工等服务。乙方须积极向客户推荐居然设计家合作品牌产品，引导消费者以及施工单位使用设计家施工管理工具。
</section>
<section class="indent">
    3.乙方在平台的合同期内总产值应不低于<div class="textput smalltextput">${c09}</div>万元。
</section>
<section class="indent">
    4.乙方对客户提供材料采购服务必须优先选择甲方已经签约的品牌，并于甲方线下市场门店成交，不得进行场外交易。
</section>
<section class="indent">
    5.乙方所提供的设计服务必须符合国家相关规定及标准，乙方应遵守并执行甲方的施工管理规定和相关业务流程。若乙方选择甲方的施工服务，则由甲方指派施工单位对预算及施工图纸进行审核，审核不通过时，乙方有义务配合施工单位对预算及施工图纸进行修改。若乙方不选择甲方提供的施工服务，则由乙方自行承担施工责任。
</section>
<section class="indent">
    6.乙方需提供甲方为其进行宣传推广的相关资料，包含但不限于个人肖像、作品及设计档案等，并积极参加甲方组织的各类宣传活动为居然设计家平台代言。合同期内活动场次不低于<div class="textput smalltextput">${c10}</div>场。
</section>
<section class="indent">
    7.乙方保证在合作期内，保持良好的个人形象和社会形象，不做任何侵犯甲方权益或损害甲方名誉的行为。
</section>
<h4>四、入驻保证金</h4>
<section class="indent">
  <#--  1.乙方登录平台提交资质，并在审核通过后5日内，向甲方支付平台入驻保证金     元，保证金缴纳遵循如下约定：
    1.1资质审核通过后，一次性缴纳     元入驻保证金；
    1.2剩余部分从接到的每一个平台项目里扣除项目总金额的  20   %充作保证金，直到剩余保证金补足为止。-->

    1.乙方登录平台提交资质，并在审核通过后5日内，向甲方支付平台入驻保证金<div class="textput smalltextput">${c15}</div>元，保证金缴纳遵循如下约定
<#list code13 as c>
    <#if (code13?size = 1)>
    <div class="indent">
        1.1资质审核通过后，一次性缴纳<div class="textput smalltextput">${c.costValue}</div>元的入驻保证金；
    </div>
    </#if>
</#list>
<#if ( code13?size > 1)>
    <div class="indent">
       <#-- 1.2资质审核通过后，在共计<div class="textput smalltextput">${c15}</div>元的入驻保证金里缴纳
      &lt;#&ndash;  <div class="textput smalltextput">${code13.[0].costValue}</div>元，
        剩余部分从接到的每一个平台项目里扣除项目总金额的
        <div class="textput smalltextput">${code13.[0].costValue}</div>
        %充作保证金，&ndash;&gt;
-->
        <#list code13 as c>
           <div class="indent">  1.${c_index+1} ${(c.costName?split("@")[0])} <div class="textput smalltextput">${c.costValue}</div>
             <#if c.cType == 1 >
                    元
             <#else>
            %
             </#if>
            ${(c.costName?split("@")[1]) }。</div>
        </#list>
    </div>
        </#if>
</section>
<section class="indent">
    2.在合同期间内，若出现由于乙方责任造成的业主投诉，则甲方有权根据实际情况从入驻保证金中扣除相应的投诉处理款项，入驻保证金不足以抵扣时，甲方有权就剩余部分予以追偿。乙方应在收到甲方扣保证金通知后，于3日内将入驻保证金补齐，每逾期一天应向甲方缴纳未交付款项的3‰作为滞纳金。若乙方超过30天未补齐保证金，则甲方有权解除合同并要求乙方承担违约责任。
</section>
<section class="indent">
    3.本合同到期时，如双方不再续约，乙方应于完成设计平台所有设计、施工项目后向甲方申请退还保证金。甲方收到乙方退换保证金申请，并确认乙方无退款或投诉纠纷的情况下，于3个月后将入驻保证金无息退还给乙方。如乙方未能将设计项目服务完成并给甲方造成损失，则甲方有权在扣除相应金额后，将入驻保证金的余额退还乙方。
</section>

<h4>五、返款及结算</h4>
<section class="indent">
    1.设计费
</section>
<section class="indent">
    <div class="indent">1.1甲方根据《设计合同》收到客户支付的相应设计费后，分两次与乙方结算：</div>
     <#list code01 as c>
        <div class="indent">  1.1.${c_index+1} ${(c.costName?split("@")[0])} <div class="textput smalltextput">${c.costValue}</div>
             <#if c.cType == 1 >
                    元
             <#else>
            %
             </#if>
            ${(c.costName?split("@")[1]) }。</div>
     </#list>
    <#--<div class="indent">1.1.1所有设计阶段完成后，平台与乙方结算设计费总额的 <div class="textput smalltextput">70</div> %。</div>
    <div class="indent">1.1.2设计转入施工，且施工结束后，平台与乙方结算设计费总额的 <div class="textput smalltextput">30</div> %。</div>-->
    <div class="indent"> 1.2 由乙方发起设计服务费对账单，甲方确认无误后返还给乙方。乙方应为客户开具符合《设计合同》约定的等额发票，并依法纳税。客户支付过程中产生的手续费由乙方承担。</div>
</section>
<section class="indent">
    2.设计平台服务管理费
    <div class="indent">2.1甲方将收取乙方与甲方客户签订的《设计合同》实收金额的<div class="textput smalltextput">${c08}</div>%作为平台服务管理费，并于每次向乙方结算时同步扣除。</div>
    <div class="indent">2.2如甲方客户发生设计订单退款，则以平台退款记录为准退还平台服务管理费。</div>
</section>
<section class="indent">
    3.材料推荐服务费
    <#--<div class="indent">3.1甲方根据市场需求签约品牌经销商，并按照乙方推荐客户签约的《销售合同》实收金额的<div class="textput smalltextput">${c23}</div>%作为材料推荐服务费（不同品类的材料推荐服务费不同，具体以网站公布为准），甲方分两次与乙方结算材料推荐服务费。</div>--><#--<div class="indent">3.1甲方根据市场需求签约品牌经销商，并按照乙方推荐客户签约的《销售合同》实收金额的<div class="textput smalltextput">${c23}</div>%作为材料推荐服务费（不同品类的材料推荐服务费不同，具体以网站公布为准），甲方分两次与乙方结算材料推荐服务费。</div>-->
    <div class="indent">3.1甲方根据市场需求签约品牌经销商，并按照乙方推荐客户签约的《销售合同》实收金额的百分比作为材料推荐服务费（不同品类的材料推荐服务费不同，具体以网站公布为准）。甲方分两次与乙方结算材料推荐服务费</div>
    <#list code09 as c>
              <div class="indent"> 3.1.${c_index+1} ${(c.costName?split("@")[0])} <div class="textput smalltextput">${c.costValue}</div>
        <#if c.cType == 1 >
                    元
        <#else>
            %
        </#if>
             ${(c.costName?split("@")[1]) }。</div>
    </#list>
<#--    <div class="indent">3.1.1甲方在客户与商家签订正式订单并付款（全额或首期款）后向乙方支付材料推荐服务费总额 <div class="textput smalltextput">30</div> %的首款。</div>
    <div class="indent">3.1.2甲方在商家为客户配送产品完成且客户确认验收的60天后向乙方支付材料推荐服务费总额 <div class="textput smalltextput">30</div> %的尾款。</div>-->
    <div class="indent">如甲方客户发生材料订单退款，则乙方应将该笔订单所得材料推荐服务费退还给甲方。如乙方未在5个工作日内予以退还，则甲方有权从下笔订单中扣除应退还的材料推荐服务费。</div>
</section>
<section class="indent">
    4.施工服务费
    <div class="indent">
        4.1甲方依据市场需求提供施工服务，并按照乙方推荐客户签约的《施工合同》实收金额的<div class="textput smalltextput">${c12}</div>%作为施工服务费。
    </div>
    <#list code10 as c>
                <div class="indent">  4.1.${c_index+1} ${(c.costName?split("@")[0])} <div class="textput smalltextput">${c.costValue}</div>
                <#if c.cType == 1 >
                            元
                <#else>
                    %
                </#if>
                 ${(c.costName?split("@")[1]) }。</div>
    </#list>
 <#--
    <div class="indent">
        4.1.1施工订单开工后，甲方向乙方支付施工服务费总额<div class="textput smalltextput"></div>%的首款。
    </div>
    <div class="indent">
        4.1.2施工竣工验收结束后，甲方向乙方支付施工服务费总额<div class="textput smalltextput"></div>%的尾款。
    </div>-->
    <div class="indent">
        4.1.6施工过程中发生合同解约，如因设计师设计原因造成，则设计师的全部施工服务费不予支付；如非由设计师造成，则施工服务费按实际发生费用支付。
    </div>
</section>
<section class="indent">
    5.甲方支付乙方设计服务费、材料推荐服务费、施工服务费时，乙方应根据法律为甲方开具税率为 6% 的增值税专用发票。如乙方无法提供定额税点发票，则甲方有权扣除税点差额部分作为对甲方税点损失的补偿。个人设计师依据《中华人民共和国个人所得税法(2018年修正本)》由甲方代扣代缴个人所得税，返款剩余部分按协议约定进行返还。
</section>
<section class="indent">
    6.上述所有付款，甲乙双方于<div class="textput smalltextput">${c13}</div>之间结算上月度款项。
</section>
<h4>六、违约责任</h4>
<section class="indent">
    1.乙方不得私下向甲方客户散布或推荐其他产品渠道或来源。乙方不应私自收取供应商回扣，一经发现则视同乙方违约，甲方将终止合同，并追偿由此给客户及甲方带来的损失。
</section>
<section class="indent">
    2.乙方不应侵犯他人的知识产权，抄袭、剽窃他人知识产权成果，如出现任何法律纠纷，则由此引发的一切赔偿责任均由乙方自行承担。
</section>
<section class="indent">
    3.乙方若未按本合同第三条第3款规定达到本合同期内总产值，则甲方有权依据实际情况提升平台服务管理费。
</section>
<section class="indent">
    4.乙方若未按本合同第三条第7款规定达到约定要求，则甲方有权解除合同终止合作，并对乙方给甲方造成的损失追究相应法律责任。
</section>
<section class="indent">
    5.客户投诉应由甲方先行接受并处理，乙方应服从甲方的处理意见，对于拒不配合解决问题者，甲方有权扣押乙方款项或使用乙方已付“入驻保证金”解决客诉。情节严重者，可视同乙方违约，甲方有权解除合同并终止合作。严禁乙方与甲方客户进行场外交易，否则视同乙方违约，甲方有权终止合同并追究乙方相应的法律责任。
</section>
<section class="indent">
    6.乙方若未履行乙方义务则视同乙方违约，甲方将终止合同，并追偿由此给客户及甲方带来的损失。
</section>
<section class="indent">
    7.甲方因故意或重大过失导致乙方利益受损时，赔偿金额以乙方实际发生的直接损失为依据进行计算。
</section>
<h4>七、免责条款</h4>
<section class="indent">
    1.鉴于平台部分服务依托于网络的特殊性，甲方在有正当理由的情况下可以随时调整或终止部分服务，并且甲方不对乙方因此产生的损失承担责任。甲方会尽可能事前通知乙方，以便乙方做好相关业务调整，保护乙方合法权益。正当理由包括如下事由：
    <div class="indent">1.1常规维护，即甲方为向用户提供更加完善的服务而定期或不定期对服务平台或相关设备进行检修、维护和升级； </div>
    <div class="indent">1.2不可抗力，包括但不限于自然灾害、政府行为、政策颁布调整、法律法规颁布调整、罢工、动乱；</div>
    <div class="indent">1.3基础运营商过错，包括但不限于电信部门技术调整、电信/电力线路被他人破坏、电信/电力部门对电信网络/电力资源进行安装、改造、维护；</div>
    <div class="indent">1.4网络安全事故，如计算机被黑客攻击或遭到病毒、木马等其他恶意程序破坏；</div>
    <div class="indent">1.5其他非甲方过错、甲方无法控制或合理预见的情形。</div>
</section>
<section class="indent">
    2.其他因乙方自身的原因导致的任何损失或责任，由乙方自行负责，甲方不承担责任。
</section>
<section class="indent">
    3.甲方对于与本合同有关或由本合同引起的任何间接性、惩罚性、特殊性或派生的损失（包括业务损失、收益损失、利润损失、使用数据或其他经济利益的损失），均不承担任何赔偿责任。
</section>
<h4>八、其他</h4>
<section class="indent">
    1.甲乙双方均有保密义务，对双方合作协议、合作过程中涉及到的信息不得向第三方泄露。合同终止或提前解除不影响保密约束义务持续有效。
</section>
<section class="indent">
    2.本协议适用中华人民共和国法律。双方就合作中出现的问题如不能达成一致意见，可提交甲方住所地人民法院解决。
</section>
<section class="indent">
    3.对本协议的所有修正、更改或补充均应以书面形式作为本协议的附件或补充协议。本协议的附件及补充协议作为本协议不可分割的一部分，与本协议具有同等法律效力。
</section>
<section class="indent">
    4.本协议一式三份，甲方两份，乙方一份。
</section>
<section class="indent">
    本协议生效日期<div class="textput smalltextput">${signedTime?string('yyyy')}</div>年
    <div class="textput smalltextput">${signedTime?string('MM')}</div>月
    <div class="textput smalltextput">${signedTime?string('dd')}</div>日。
</section>

<div class="footer">
    <h4 style="display:inline-block">甲方（盖章）：</h4>
    <h4 style="margin-left: 28%;display:inline-block">乙方（盖章）：</h4>
</div>
<div class="footer">
    <h4 style="display:inline-block">签约代理人（签字）：</h4>
    <h4 style="margin-left: 25.5%;display:inline-block">签约代理人（签字）：</h4>
</div>
<div class="footer">
    <h4 style="display:inline-block">联系电话：</h4>
    <h4 style="margin-left: 30%;display:inline-block">联系电话：</h4>
</div>
<div class="footer">
    <h4  style="display:inline-block">日期：<div class="textput smalltextput"></div> 年<div class="textput smalltextput"></div> 月<div class="textput smalltextput"></div> 日</h4>
    <h4 style="margin-left: 25.5%;display:inline-block">日期：<div class="textput smalltextput"></div>年<div class="textput smalltextput"></div>月<div class="textput smalltextput"></div>日</h4>
</div>
<p>附件1:</p>
<div class="text-algin">居然设计家设计行业公约</div>
<div class="text-algin">
    <p>关注民生 以提高公众生活质量为出发点，打造高品质的生活环境；</p>
    <p>助推行业 以推动行业发展为己任，提升公众对设计的认识和理解；</p>
    <p>保护生态 以绿色环保为原则，杜绝设计施工中私搭乱建和环境污染；</p>
    <p>珍视操守 以设计师职业道德为基准，避免恶性竞争和不公平交易；</p>
    <p>恪守诚信 以诚信服务为宗旨，严格履行合同，保障客户正当权益</p>
    <p>合理收益 以实现合理收益为方向，杜绝设计施工中的不正当收益；</p>
    <p>提升自我 以自身素质提高为目标，注重学习积累，不断提高自我。</p>
</div>
</body>

</html>