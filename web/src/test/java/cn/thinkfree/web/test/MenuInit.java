package cn.thinkfree.web.test;

import cn.thinkfree.database.constants.MenuType;
import cn.thinkfree.database.mapper.MenuMapper;
import cn.thinkfree.database.mapper.SystemPermissionResourceMapper;
import cn.thinkfree.database.mapper.SystemResourceMapper;
import cn.thinkfree.database.model.Menu;
import cn.thinkfree.database.model.SystemPermissionResource;
import cn.thinkfree.database.model.SystemResource;
import cn.thinkfree.database.vo.IndexMenuVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class MenuInit extends AbsControllerTest{

    @Autowired
    MenuMapper menuMapper;

    @Test
    public void init(){
        String json ="[\n" +
                "      {\n" +
                "        \"path\": \"/company\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/company/company\",\n" +
                "        \"name\": \"company\",\n" +
                "        \"meta\": {\n" +
                "          \"title\": \"公司管理\",\n" +
                "          \"icon\": \"nested\"\n" +
                "        },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"/company/company\",\n" +
                "            \"name\": \"company\",\n" +
                "            \"component\": \"/company/company\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"公司入驻管理\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/account\",\n" +
                "            \"name\": \"account\",\n" +
                "            \"component\": \"/company/account\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"添加公司\"\n" +
                "            },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/accountDR\",\n" +
                "            \"name\": \"accountDR\",\n" +
                "            \"component\": \"/company/accountDR\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"添加公司\"\n" +
                "            },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"design\",\n" +
                "            \"name\": \"design\",\n" +
                "            \"component\": \"/company/design/design\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"设计公司管理\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/design/design-contract\",\n" +
                "            \"name\": \"designContract\",\n" +
                "            \"component\": \"/company/design/design-contract\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"查看入驻合同\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/design/design-details\",\n" +
                "            \"name\": \"design-details\",\n" +
                "            \"component\": \"/company/design/design-details\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"公司详情\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/decorate/decorate\",\n" +
                "            \"name\": \"design-details\",\n" +
                "            \"component\": \"/company/decorate/decorate/index\",\n" +
                "            \"meta\": {\n" +
                "              \"title\": \"装饰公司管理\"\n" +
                "            },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"decorate/decorate-change\",\n" +
                "            \"component\": \"/company/decorate/decorate-change/index\",\n" +
                "            \"name\": \"decorateChange\",\n" +
                "            \"meta\": { \"title\": \"资质变更审核\" },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/decorate/decorate-contract\",\n" +
                "            \"component\": \"/company/decorate/decorate-contract/index\",\n" +
                "            \"name\": \"decorateContract\",\n" +
                "            \"meta\": { \"title\": \"查看入驻合同\" },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/company/decorate/decorate-details\",\n" +
                "            \"component\": \"/company/decorate/decorate-details/index\",\n" +
                "            \"name\": \"decorateDetails\",\n" +
                "            \"meta\": { \"title\": \"装饰合同详情\" },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"dealer\",\n" +
                "            \"component\": \"/company/index\",\n" +
                "            \"name\": \"Dealer\",\n" +
                "            \"meta\": { \"title\": \"经销商管理\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"dealer\",\n" +
                "                \"component\": \"/company/dealer/dealer/index\",\n" +
                "                \"name\": \"dealer\",\n" +
                "                \"meta\": { \"title\": \"经销商\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"dealerDetails\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/dealer-details/index\",\n" +
                "                \"name\": \"dealerDetails\",\n" +
                "                \"meta\": { \"title\": \"公司详情\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/distributorContract\",\n" +
                "                \"component\": \"/company/dealer/distributorContract/index\",\n" +
                "                \"name\": \"distributorContract\",\n" +
                "                \"meta\": { \"title\": \"经销商合同列表\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/entryContract\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/entryContract/index\",\n" +
                "                \"name\": \"entryContract\",\n" +
                "                \"meta\": { \"title\": \"录入合同\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/entryContractNew\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/entryContract/indexNew\",\n" +
                "                \"name\": \"entryContract\",\n" +
                "                \"meta\": { \"title\": \"录入合同\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/dealer-change\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/dealer-change/index\",\n" +
                "                \"name\": \"dealerChange\",\n" +
                "                \"meta\": { \"title\": \"变更合同\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/dealer-renew\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/dealer-renew/index\",\n" +
                "                \"name\": \"dealerRenew\",\n" +
                "                \"meta\": { \"title\": \"续签合同\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/dealer-contract\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/dealer-contract/index\",\n" +
                "                \"name\": \"dealerContract\",\n" +
                "                \"meta\": { \"title\": \"查看合同\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/print\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/print\",\n" +
                "                \"name\": \"dealerContract\",\n" +
                "                \"meta\": { \"title\": \"打印合同\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/operationApproval\",\n" +
                "                \"component\": \"/company/dealer/operationApproval/index\",\n" +
                "                \"name\": \"operationApproval\",\n" +
                "                \"meta\": { \"title\": \"运营审批管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/contractDetails\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/company/dealer/contractDetails/index\",\n" +
                "                \"name\": \"contractDetails\",\n" +
                "                \"meta\": { \"title\": \"合同详情\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/company/dealer/examineApprove\",\n" +
                "                \"component\": \"/company/dealer/examineApprove/index\",\n" +
                "                \"name\": \"examineApprove\",\n" +
                "                \"meta\": { \"title\": \"财务审批管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"QualificationInspecting\",\n" +
                "                \"component\": \"/company/dealer/QualificationInspecting/index\",\n" +
                "                \"name\": \"QualificationInspecting\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"资质审核\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"BrandAudit\",\n" +
                "                \"component\": \"/company/dealer/BrandAudit/index\",\n" +
                "                \"name\": \"BrandAudit\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"品牌审核\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/designPlatform\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/designPlatform/designerList\",\n" +
                "        \"name\": \"DesignPlatform\",\n" +
                "        \"meta\": { \"title\": \"设计平台\", \"icon\": \"example\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"designerList\",\n" +
                "            \"name\": \"designerList\",\n" +
                "            \"component\":\"/designPlatform/designerList/index\",\n" +
                "            \"meta\": { \"title\": \"设计师列表\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/createOrder\",\n" +
                "            \"name\": \"createOrder\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/designPlatform/designerList/createOrder\",\n" +
                "            \"meta\": { \"title\": \"创建订单\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"establish\",\n" +
                "            \"name\": \"PlatformEstablish\",\n" +
                "            \"component\": \"/designPlatform/designerList/certification/establish/index\",\n" +
                "            \"meta\": { \"title\": \"添加设计师\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"details\",\n" +
                "            \"name\": \"PlatformDetails\",\n" +
                "            \"component\": \"/designPlatform/designerList/certification/details/index\",\n" +
                "            \"meta\": { \"title\": \"设计师详情\" },\n" +
                "            \"hidden\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"dispatch\",\n" +
                "            \"name\": \"PlatformDispatch\",\n" +
                "            \"component\": \"/designPlatform/dispatch/index\",\n" +
                "            \"meta\": { \"title\": \"设计派单管理\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"orderManagement\",\n" +
                "            \"name\": \"PlatformOrderManagement\",\n" +
                "            \"component\": \"/designPlatform/orderManagement/index\",\n" +
                "            \"meta\": { \"title\": \"设计订单管理\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"designOrderDetails\",\n" +
                "            \"hidden\": true,\n" +
                "            \"name\": \"designOrderDetails\",\n" +
                "            \"component\": \n" +
                "                \"/designPlatform/orderManagement/designOrderDetails/index\"\n" +
                "              ,\n" +
                "            \"meta\": { \"title\": \"设计订单详情\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"contract\",\n" +
                "            \"name\": \"PlatformContract\",\n" +
                "            \"component\": \"/designPlatform/contract/index\",\n" +
                "            \"meta\": { \"title\": \"设计合同管理\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/construction\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/construction\",\n" +
                "        \"alwaysShow\": true,\n" +
                "        \"meta\": { \"title\": \"施工平台\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"decorationCompany\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/construction/decorationCompany/index\",\n" +
                "            \"name\": \"decorationCompany\",\n" +
                "            \"meta\": { \"title\": \"装饰公司管理\" },\n" +
                "            \"alwaysShow\": true,\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"/account\",\n" +
                "                \"name\": \"accountCompany\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/construction/decorationCompany/account/index\",\n" +
                "                \"meta\": { \"title\": \"账户管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/found\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\":\"/construction/decorationCompany/account/found/index\"\n" +
                "                  ,\n" +
                "                \"name\": \"found\",\n" +
                "                \"meta\": { \"title\": \"账户创建\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/see\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/construction/decorationCompany/account/see/index\",\n" +
                "                \"name\": \"see\",\n" +
                "                \"meta\": { \"title\": \"账户查看\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/aptitude\",\n" +
                "                \"component\": \"/construction/decorationCompany/aptitude/index\",\n" +
                "                \"name\": \"Aptitude\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"资质审核\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/aptitude/aptitude\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/construction/decorationCompany/aptitude/aptitude\" ,\n" +
                "                \"name\": \"aptitude\",\n" +
                "                \"meta\": { \"title\": \"资质审核\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/contract\",\n" +
                "                \"component\": \"/construction/decorationCompany/contract/index\",\n" +
                "                \"name\": \"Contract\",\n" +
                "                \"meta\": { \"title\": \"合同管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/contract/contract\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/construction/decorationCompany/contract/contract\" ,\n" +
                "                \"name\": \"contract\",\n" +
                "                \"meta\": { \"title\": \"资质审核\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"construction/decorators\",\n" +
                "            \"component\": \"/construction/decorators\",\n" +
                "            \"name\": \"decorators\",\n" +
                "            \"meta\": { \"title\": \"装饰人员\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"/construction/decorators/personnel\",\n" +
                "                \"component\": \"/construction/decorators/personnel/index\",\n" +
                "                \"name\": \"decoratorspersonnel\",\n" +
                "                \"meta\": { \"title\": \"人员管理\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/constructionOrder\",\n" +
                "            \"component\": \"/construction/constructionOrder\",\n" +
                "            \"meta\": { \"title\": \"施工订单\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"project\",\n" +
                "                \"component\": \"/construction/constructionOrder/project/index\",\n" +
                "                \"name\": \"project\",\n" +
                "                \"meta\": { \"title\": \"项目派单\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"constructionOrder/manage\",\n" +
                "                \"component\": \"/construction/constructionOrder/manage\",\n" +
                "                \"name\": \"manage\",\n" +
                "                \"meta\": { \"title\": \"订单管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"manage/details\",\n" +
                "                \"component\": \"/construction/constructionOrder/manage/details/orderDetail\",\n" +
                "                \"hidden\": true,\n" +
                "                \"name\": \"manageDetails\",\n" +
                "                \"meta\": { \"title\": \"订单详情\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/construction/constructionSite\",\n" +
                "            \"component\": \"/construction\",\n" +
                "            \"name\": \"constructionSite\",\n" +
                "            \"meta\": { \"title\": \"施工工地\" },\n" +
                "            \"alwaysShow\": true,\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"/constructionDetails/constructionSite\",\n" +
                "                \"component\": \"/construction/constructionSite/index\",\n" +
                "                \"name\": \"SiteconstructionDetails\",\n" +
                "                \"meta\": { \"title\": \"工地列表\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"/constructionSite/constructionDetails\",\n" +
                "                \"component\":  \"/construction/constructionSite/constructionDetails\",\n" +
                "                \"name\": \"SiteconstructionDetails1\",\n" +
                "                \"meta\": { \"title\": \"工地详情\" },\n" +
                "                \"hidden\": true\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/finance\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/finance\",\n" +
                "        \"meta\": { \"title\": \"财务管理\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"settlementRules\",\n" +
                "            \"component\": \"/finance/settlementRules/index\",\n" +
                "            \"name\": \"settlementRules\",\n" +
                "            \"meta\": { \"title\": \"结算规则\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"proportions\",\n" +
                "                \"component\": \"/finance/settlementRules/proportions/index\",\n" +
                "                \"name\": \"FinanceProportions\",\n" +
                "                \"meta\": { \"title\": \"比例设置\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"createRules\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/proportions/createRules/index\",\n" +
                "                \"name\": \"createRules\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"创建比例规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"viewRules\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/proportions/viewRules/index\",\n" +
                "                \"name\": \"viewRules\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"查看比例规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"copyRule\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/proportions/copyRule/index\",\n" +
                "                \"name\": \"copyRule\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"拷贝比例规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"cancelRule\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/proportions/cancelRule/index\",\n" +
                "                \"name\": \"cancelRule\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"作废比例规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"ruleSetting\",\n" +
                "                \"component\": \"/finance/settlementRules/ruleSetting/index\",\n" +
                "                \"name\": \"ruleSetting\",\n" +
                "                \"meta\": { \"title\": \"结算规则设置\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"createReceivables\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/ruleSetting/createReceivables/index\",\n" +
                "                \"name\": \"createReceivables\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"创建代收款结算规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"checkReceivables\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/ruleSetting/createReceivables/checkReceivables/index\",\n" +
                "                \"name\": \"checkReceivables\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"查看代收款结算规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"cancelledCollection\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/ruleSetting/createReceivables/cancelledCollection/index\",\n" +
                "                \"name\": \"cancelledCollection\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"作废代收款结算规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"copyCollection\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/ruleSetting/createReceivables/copyCollection/index\",\n" +
                "                \"name\": \"copyCollection\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"拷贝代收款结算规则\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"createChargingRules\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/settlementRules/ruleSetting/createChargingRules/index\",\n" +
                "                \"name\": \"createChargingRules\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"创建平台收费结算规则\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"diurnalknot\",\n" +
                "            \"component\": \"/finance/diurnalknot\",\n" +
                "            \"name\": \"Diurnalknot\",\n" +
                "            \"meta\": { \"title\": \"日结\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"diurnalknot/diurnalknot\",\n" +
                "                \"component\": \"/finance/diurnalknot/diurnalknot/index\",\n" +
                "                \"name\": \"diurnalknot\",\n" +
                "                \"meta\": { \"title\": \"日结流水表操作\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"checkErrors\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/diurnalknot/diurnalknot/checkErrors/index\",\n" +
                "                \"hidden\": true,\n" +
                "                \"name\": \"checkErrors\",\n" +
                "                \"meta\": { \"title\": \"查看日结差错对账\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"editingError\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \n" +
                "                    \"/finance/diurnalknot/diurnalknot/editingError/index\",\n" +
                "                \"name\": \"editingError\",\n" +
                "                \"meta\": { \"title\": \"差错对账编辑\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"designerReview\",\n" +
                "            \"component\": \"/finance/designerReview/index\",\n" +
                "            \"name\": \"designerReview\",\n" +
                "            \"meta\": { \"title\": \"设计师提成审核\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"incomeAudit\",\n" +
                "            \"component\": \"/finance/incomeAudit/index\",\n" +
                "            \"name\": \"incomeAudit\",\n" +
                "            \"meta\": { \"title\": \"平台管理服务费审核\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"productService\",\n" +
                "            \"component\": \"/finance/productService/index\",\n" +
                "            \"name\": \"productService\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"产品服务费审核\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"transactionSettlement\",\n" +
                "            \"component\": \"/finance/transactionSettlement\",\n" +
                "            \"name\": \"Settlement\",\n" +
                "            \"meta\": { \"title\": \"交易结算\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"transactionSettlement\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/transactionSettlement/transactionSettlement/index\",\n" +
                "                \"name\": \"transactionSettlement\",\n" +
                "                \"meta\": { \"title\": \"交易结算\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"receivablesTabs\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \n" +
                "                    \"/finance/transactionSettlement/transactionSettlement/receivablesTabs/index\",\n" +
                "                \"name\": \"receivablesTabs\",\n" +
                "                \"meta\": { \"title\": \"查看收款明细\" }\n" +
                "              },\n" +
                "    \n" +
                "              {\n" +
                "                \"path\": \"receiptsDetails\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \n" +
                "                    \"/finance/transactionSettlement/transactionSettlement/receiptsDetails/index\",\n" +
                "                \"name\": \"receiptsDetails\",\n" +
                "                \"meta\": { \"title\": \"收款明细查看\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"manualSettlement\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \n" +
                "                    \"/finance/transactionSettlement/transactionSettlement/manualSettlement/index\",\n" +
                "                \"name\": \"manualSettlement\",\n" +
                "                \"meta\": { \"title\": \"手动申请结算\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"voidStatement\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \n" +
                "                    \"/finance/transactionSettlement/transactionSettlement/voidStatement/index\",\n" +
                "                \"name\": \"voidStatement\",\n" +
                "                \"meta\": { \"title\": \"作废对账单\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"DesignerCommission\",\n" +
                "            \"component\": \"/finance/designerCommission/index\",\n" +
                "            \"hidden\": false,\n" +
                "            \"name\": \"designerCommission\",\n" +
                "            \"meta\": { \"title\": \"设计师提成结算\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"designerCommission\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/designerCommission/designerCommission/index\",\n" +
                "                \"name\": \"designerCommission\",\n" +
                "                \"meta\": { \"title\": \"设计师提成结算\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"royaltySettlement\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/designerCommission/royaltySettlement/index\",\n" +
                "                \"hidden\": true,\n" +
                "                \"name\": \"royaltySettlement\",\n" +
                "                \"meta\": { \"title\": \"查看施工服务费\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"toVoid\",\n" +
                "                \"component\": \"/finance/designerCommission/toVoid/index\",\n" +
                "                \"name\": \"toVoid\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"作废对账单\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"marginManagement\",\n" +
                "            \"component\": \"/finance/marginManagement\",\n" +
                "            \"name\": \"marginManagement\",\n" +
                "            \"meta\": { \"title\": \"保证金管理\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"bond\",\n" +
                "                \"component\": \"/finance/marginManagement/bond/index\",\n" +
                "                \"name\": \"bond\",\n" +
                "                \"meta\": { \"title\": \"保证金管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"marginDetails\",\n" +
                "                \"component\": \n" +
                "                    \"/finance/marginManagement/bond/marginDetails/index\",\n" +
                "                \"hidden\": true,\n" +
                "                \"name\": \"marginDetails\",\n" +
                "                \"meta\": { \"title\": \"详情\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"otherCharges\",\n" +
                "            \"component\": \"/finance/otherCharges\",\n" +
                "            \"name\": \"otherCharges\",\n" +
                "            \"meta\": { \"title\": \"平台其他收费项目\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"payService\",\n" +
                "                \"component\": \"/finance/otherCharges/payService/index\",\n" +
                "                \"name\": \"ChargespayService\",\n" +
                "                \"meta\": { \"title\": \"平台其他收费项目\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"see\",\n" +
                "                \"component\": \"/finance/otherCharges/payService/see/index\",\n" +
                "                \"name\": \"checkIndex\",\n" +
                "                \"hidden\": true,\n" +
                "                \"meta\": { \"title\": \"查看\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"reportForm\",\n" +
                "            \"component\": \"/finance/reportForm\",\n" +
                "            \"name\": \"reportForm\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"报表\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"orderReport\",\n" +
                "                \"component\": \"/finance/reportForm/orderReport/index\",\n" +
                "                \"name\": \"orderReport\",\n" +
                "                \"meta\": { \"title\": \"订单报表\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"incomeStatement\",\n" +
                "                \"component\": \"/finance/reportForm/incomeStatement/index\",\n" +
                "                \"name\": \"incomeStatement\",\n" +
                "                \"meta\": { \"title\": \"平台管理服务费报表\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"businessStatement\",\n" +
                "                \"component\": \"/finance/reportForm/businessStatement/index\",\n" +
                "                \"name\": \"businessStatement\",\n" +
                "                \"meta\": { \"title\": \"商户返款报表\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"billingStatement\",\n" +
                "                \"component\": \"/finance/reportForm/billingStatement/index\",\n" +
                "                \"name\": \"billingStatement\",\n" +
                "                \"meta\": { \"title\": \"对账单报表\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "    \n" +
                "          {\n" +
                "            \"path\": \"/examineIndex\",\n" +
                "            \"component\": \"/finance/examine/index\",\n" +
                "            \"name\": \"Examine\",\n" +
                "            \"meta\": { \"title\": \"合同审批管理\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"examine\",\n" +
                "                \"component\": \"/finance/examine/examine\",\n" +
                "                \"name\": \"examine\",\n" +
                "                \"meta\": { \"title\": \"合同审批管理\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"details\",\n" +
                "                \"name\": \"details\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/finance/examine/details\",\n" +
                "                \"meta\": { \"title\": \"合同详情\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"refund\",\n" +
                "            \"component\": \"/finance/refund\",\n" +
                "            \"name\": \"Examine\",\n" +
                "            \"meta\": { \"title\": \"退款与赔付处理\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/accountList\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/accountList/index\",\n" +
                "        \"meta\": { \"title\": \"用户账号管理\", \"icon\": \"accountM\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"certification\",\n" +
                "            \"name\": \"PlatformCertification\",\n" +
                "            \"component\": \"/accountList/certification/index\",\n" +
                "            \"meta\": { \"title\":\"员工实名认证\", \"icon\": \"accounts\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/accountList/manage\",\n" +
                "            \"name\": \"PlatformManage\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/accountList/certification/manage/index\",\n" +
                "            \"meta\": { \"title\": \"实名审核详情\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/accountList\",\n" +
                "            \"component\": \"/platform/accountList\",\n" +
                "            \"name\": \"accountList\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"入驻公司账号管理\", \"icon\": \"accounts\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"RoleSet\",\n" +
                "            \"name\": \"RoleSet\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/RoleSet/index\",\n" +
                "            \"meta\": { \"title\": \"服务商角色设置\", \"icon\": \"tree\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/branchStation\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/platform/branchStation\",\n" +
                "        \"meta\": { \"title\": \"站点管理\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"substationManagement\",\n" +
                "            \"component\": \"/platform/substationManagement\",\n" +
                "           \" name\": \"substationManagement\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"分站管理\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/substationCreate\",\n" +
                "            \"component\": \"/platform/substationCreate\",\n" +
                "            \"name\": \"substationCreate\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"创建分站\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/branchStation/index\",\n" +
                "            \"component\": \"/platform/branchStation/index\",\n" +
                "            \"name\": \"branchStation\",\n" +
                "            \"meta\": { \"title\": \"分公司站点\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/branchStation/substationSeeAbout\",\n" +
                "            \"component\": \"/platform/branchStation/substationSeeAbout\",\n" +
                "            \"name\": \"branchStationAbout\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"分站详情\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/citySubstation/index\",\n" +
                "            \"component\": \"/platform/citySubstation/index\",\n" +
                "            \"name\": \"citySubstation\",\n" +
                "            \"meta\": { \"title\": \"城市分站\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/citySubstation/citySeeAbout\",\n" +
                "            \"component\": \"/platform/citySubstation/citySeeAbout\",\n" +
                "            \"name\": \"citySeeAbout\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"分站详情\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/site\",\n" +
                "            \"component\": \"/platform/site\",\n" +
                "            \"name\": \"site\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"站点信息\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/businessData/index\",\n" +
                "            \"component\": \"/platform/businessData/index\",\n" +
                "            \"name\": \"businessData\",\n" +
                "            \"meta\": { \"title\": \"经营主体数据\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/businessData/businessSeeAbout\",\n" +
                "            \"component\": \"/platform/businessData/businessSeeAbout\",\n" +
                "            \"name\": \"businessSeeAbout\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"主体详情\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/platform\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"name\": \"platformAuthority\",\n" +
                "        \"meta\": { \"title\": \"系统权限管理\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"authority\",\n" +
                "            \"component\": \"/platform/authority\",\n" +
                "            \"name\": \"platformAuthority3\",\n" +
                "            \"meta\": { \"title\": \"权限管理\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"role\",\n" +
                "            \"component\": \"/platform/role\",\n" +
                "            \"name\": \"role\",\n" +
                "            \"meta\": { \"title\": \"角色管理\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/accountManagement/inde\",\n" +
                "            \"component\": \"/platform/accountManagement/index\",\n" +
                "            \"name\": \"platformAuthority1\",\n" +
                "            \"meta\": { \"title\": \"账号管理\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/platform/accountManagement/accountAdd\",\n" +
                "            \"component\": \"/platform/accountManagement/accountAdd\",\n" +
                "            \"name\": \"platformAuthority2\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"添加管理员账号 \" }\n" +
                "          },\n" +
                "    \n" +
                "          {\n" +
                "            \"path\": \"/platform/allocation\",\n" +
                "            \"component\": \"/platform/allocation\",\n" +
                "            \"name\": \"allocation\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"功能分配\" }\n" +
                "          },\n" +
                "    \n" +
                "          {\n" +
                "            \"path\": \"/platform/constractionRole\",\n" +
                "            \"component\": \"/platform/constractionRole\",\n" +
                "            \"name\": \"constractionRole\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"施工角色设置\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/contractPlatform\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/platform/index\",\n" +
                "        \"alwaysShow\": true,\n" +
                "        \"meta\": { \"title\": \"平台设置\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"contractTemplate\",\n" +
                "            \"component\": \"/platform/contractTemplate\",\n" +
                "            \"name\": \"contractTemplate\",\n" +
                "            \"meta\": { \"title\": \"平台设置\" },\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"path\": \"index\",\n" +
                "                \"component\": \"/platform/contractTemplate/index\",\n" +
                "                \"name\": \"contractTemplateIndex\",\n" +
                "                \"meta\": { \"title\": \"合同模板设置\" }\n" +
                "              },\n" +
                "              {\n" +
                "                \"path\": \"contractEdit\",\n" +
                "                \"hidden\": true,\n" +
                "                \"component\": \"/platform/contractTemplate/contractEdit\",\n" +
                "                \"name\": \"contractEdit\",\n" +
                "                \"meta\": { \"title\": \"合同模板编辑\" }\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"designBasis\",\n" +
                "        \"component\": \"/platform/designBasis\",\n" +
                "        \"name\":\"designBasis\",\n" +
                "        \"meta\": { \"title\": \"设计师管理\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"designLevel\",\n" +
                "            \"component\": \"/platform/designLevel\",\n" +
                "            \"name\":\"designLevel\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"设计师等级设置\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"designBasischildren\",\n" +
                "            \"component\": \"/platform/designBasis\",\n" +
                "            \"name\": \"designBasis\",\n" +
                "            \"meta\": { \"title\": \"设计基础设置\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"contrationScheme/addScheme\",\n" +
                "            \"component\": \"/platform/contrationScheme/addScheme\",\n" +
                "            \"name\":\"addScheme\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"tab配置\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"constructionPlan\",\n" +
                "        \"component\": \"/platform/constructionPlan/index\",\n" +
                "        \"name\": \"constructionPlanPlan\",\n" +
                "        \"meta\": { \"title\": \"施工方案\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"constructionPlanList\",\n" +
                "            \"component\": \"constructionPlan/constructionPlan\",\n" +
                "            \"name\": \"constructionPlanP\",\n" +
                "            \"meta\": { \"title\": \"施工方案列表\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"constructionItem\",\n" +
                "            \"component\": \"/constructionPlan/constructionItem\",\n" +
                "            \"name\": \"constructionItem\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"施工方案配置\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"construction\",\n" +
                "            \"component\": \"/constructionPlan/construction\",\n" +
                "            \"name\": \"construction\",\n" +
                "            \"hidden\": true,\n" +
                "            \"meta\": { \"title\": \"施工管理基础设置\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"changePassword\",\n" +
                "        \"component\": \"/platform/changePassword\",\n" +
                "        \"name\": \"changePassword\",\n" +
                "        \"hidden\": true,\n" +
                "        \"meta\": { \"title\": \"修改密码\" }\n" +
                "      },\n" +
                "\n" +
                "      {\n" +
                "        \"path\": \"substationEdit\",\n" +
                "        \"component\": \"/platform/substationEdit\",\n" +
                "        \"name\": \"substationEdit\",\n" +
                "        \"hidden\": true,\n" +
                "        \"meta\": { \"title\": \"编辑管理\" }\n" +
                "      },\n" +
                "      {\n" +
                "        \"path\": \"/customService\",\n" +
                "        \"component\": \"Layout\",\n" +
                "        \"redirect\": \"/customService/customService\",\n" +
                "        \"name\": \"customService\",\n" +
                "        \"meta\": { \"title\": \"客服中心\", \"icon\": \"nested\" },\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"path\": \"/customService/customService\",\n" +
                "            \"component\": \"/customService/customService\",\n" +
                "            \"name\": \"customService\",\n" +
                "            \"meta\": { \"title\": \"投诉工单\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/customerDetails\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/customService/customerDetails/index\",\n" +
                "            \"name\": \"customerDetails \",\n" +
                "            \"meta\": { \"title\": \"查看详情\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/estabLish\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/customService/estabLish/index\",\n" +
                "            \"name\": \"estabLish\",\n" +
                "            \"meta\": { \"title\": \"创建投诉工单\" }\n" +
                "          },\n" +
                "          {\n" +
                "            \"path\": \"/handle\",\n" +
                "            \"hidden\": true,\n" +
                "            \"component\": \"/customService/handle/index\",\n" +
                "            \"name\": \"handle\",\n" +
                "            \"meta\": { \"title\": \"处理投诉单\" }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                " \n" +
                "    ]";

        Gson gson = new Gson();

        List<TempMenuVO> list = gson.fromJson(json,new TypeToken<List<TempMenuVO>>(){}.getType());
        AtomicInteger id = new AtomicInteger(1);
        for (int o =0;o<list.size();o++) {
            TempMenuVO m = list.get(o);

            Menu saveObj = new Menu();
            saveObj.setSortNum(o);
            saveObj.setId(id.getAndAdd(1));
            if (m.getMeta() != null) {
                saveObj.setIcon(m.getMeta().getIcon());
                saveObj.setName(m.getMeta().getTitle());
            }
            saveObj.setPlatform(1);
            saveObj.setUrl(m.getPath());
            saveObj.setCode(m.getName());

            if (!m.isHidden()) {
                menuMapper.insert(saveObj);
            }
            for (int j = 0; j < m.getChildren().size(); j++) {
                TempMenuVO c = m.getChildren().get(j);
                Menu childObj = new Menu();
                childObj.setId(id.getAndAdd(1));
                if (c.getMeta() != null) {
                    childObj.setIcon(c.getMeta().getIcon());
                    childObj.setName(c.getMeta().getTitle());
                }
                childObj.setSortNum(j);
                childObj.setPlatform(1);
                childObj.setUrl(c.getPath());
                childObj.setCode(c.getName());
                childObj.setPid(saveObj.getId());
                if (!c.isHidden()) {
                    menuMapper.insert(childObj);
                }
                for (int i = 0; i < c.getChildren().size(); i++) {
                    TempMenuVO f = c.getChildren().get(i);

                    Menu finalObj = new Menu();
                    finalObj.setId(id.getAndAdd(1));
                    if (f.getMeta() != null) {
                        finalObj.setIcon(f.getMeta().getIcon());
                        finalObj.setName(f.getMeta().getTitle());
                    }
                    finalObj.setSortNum(i);
                    finalObj.setPlatform(1);
                    finalObj.setUrl(f.getPath());
                    finalObj.setCode(f.getName());
                    finalObj.setPid(childObj.getId());
                    if (!f.isHidden()) {
                        menuMapper.insert(finalObj);
                    }

                }
            }

        }

        System.out.println(list);

    }

    @Autowired
    SystemResourceMapper systemResourceMapper;

    @Test
    public void initResource(){

        List<Menu> menus = menuMapper.selectByExample(null);
        List<Menu> root = menus.stream().filter(m -> MenuType.ROOT.code.equals(m.getPid())).collect(Collectors.toList());
        List<IndexMenuVO> ms = root.stream().map(r -> new IndexMenuVO(r, menus)).collect(Collectors.toList());
        AtomicInteger id = new AtomicInteger(1);

        for (int o=0;o<ms.size();o++) {
            IndexMenuVO m = ms.get(o);
            SystemResource saveObj = new SystemResource();
            saveObj.setModule(id.getAndAdd(1) + "");
            saveObj.setPid(m.getPid());
            saveObj.setSortNum(o);
            saveObj.setName(m.getName());
            saveObj.setId(m.getId());
            saveObj.setType("1");
            saveObj.setCode(m.getResourceCode());
            saveObj.setPlatform(1);
            systemResourceMapper.insert(saveObj);

            for (int i = 0; i < m.getChild().size(); i++) {
                IndexMenuVO cm = m.getChild().get(i);
                SystemResource cObj = new SystemResource();
                cObj.setModule(id.get() + "");
                cObj.setPid(cm.getPid());
                cObj.setSortNum(i);
                cObj.setName(cm.getName());
                cObj.setId(cm.getId());
                cObj.setType("1");
                cObj.setCode(cm.getResourceCode());
                cObj.setPlatform(1);
                systemResourceMapper.insert(cObj);
                for (int j = 0; j < cm.getChild().size(); j++) {
                    IndexMenuVO fm = cm.getChild().get(j);
                    SystemResource fObj = new SystemResource();
                    fObj.setModule(id.get() + "");
                    fObj.setPid(fm.getPid());
                    fObj.setSortNum(j);
                    fObj.setName(fm.getName());
                    fObj.setId(fm.getId());
                    fObj.setType("1");
                    fObj.setCode(fm.getResourceCode());
                    fObj.setPlatform(1);
                    systemResourceMapper.insert(fObj);
                }
            }

        }

    }

    @Autowired
    SystemPermissionResourceMapper systemPermissionResourceMapper;
    @Test
    public void initPermissionResource(){
        AtomicInteger id = new AtomicInteger(1);
        systemResourceMapper.selectByExample(null).forEach(r->{
            SystemPermissionResource saveObj = new SystemPermissionResource();
            saveObj.setId(id.getAndAdd(1));
            saveObj.setPermissionId(1);
            saveObj.setResourceId(r.getId());
            systemPermissionResourceMapper.insert(saveObj);
        });
    }



}
